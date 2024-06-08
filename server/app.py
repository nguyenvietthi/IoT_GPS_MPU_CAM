from flask import Flask, render_template, request, make_response, jsonify, redirect, Response
import requests
import cv2
import numpy as np
import threading
from flask_cors import CORS
import secrets
import sqlite3

from io import BytesIO
from PIL import Image
from base64 import b64encode

def get_db_connection():
    conn = sqlite3.connect('instance/database.db')
    conn.row_factory = sqlite3.Row
    return conn

def insert_user(email, password, cookie):
    with get_db_connection() as conn:
        conn.execute('INSERT INTO account (email, password, cookie) VALUES (?, ?, ?)', (email, password, cookie))
        conn.commit()

def update_location(api_key, lat, lng, speed, sat, alt, date, time):
    with get_db_connection() as conn:
        conn.execute('INSERT INTO gps (device_id, lat, lng, speed, sat, alt, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)', (api_key, lat, lng, speed, sat, alt, date + " " + time))
        conn.commit()
        
def update_cookie(email, cookie):
    with get_db_connection() as conn:
        conn.execute('UPDATE account SET cookie = ? WHERE email = ?', (cookie, email))
        conn.commit()

def update_logout(email, cookie):
    with get_db_connection() as conn:
        conn.execute('UPDATE account SET cookie = "None" WHERE email = ? AND cookie = ?', (email, cookie))
        conn.commit()

def email_name_exists(email, cookie):
    with get_db_connection() as conn:
        cursor = conn.cursor()
        cursor.execute('SELECT COUNT(*) FROM account WHERE email = ? AND cookie == ?', (email, cookie))
        result = cursor.fetchone()[0]
        return result > 0
    

def get_last_location(device_id):
    with get_db_connection() as conn:
        cursor = conn.cursor()

        query = '''
        SELECT *
        FROM gps
        WHERE device_id = ?
        ORDER BY datetime DESC
        LIMIT 1
        '''
        cursor.execute(query, (device_id,))
        row = cursor.fetchone()
        return row
    
print(get_last_location("tPmAT5Ab3j7F9")[1])

def get_info(email, cookie):
    with get_db_connection() as conn:
        cursor = conn.cursor()

        query = '''
        SELECT name
        FROM account
        WHERE email = ?
        AND cookie = ?
        '''
        cursor.execute(query, (email, cookie,))
        row = cursor.fetchone()
        return row


def get_location_list(email, cookie, start_time, finish_time, device_id):
    with get_db_connection() as conn:
        cursor = conn.cursor()

        query = '''
        SELECT lat, lng, datetime
        FROM gps
        WHERE datetime > ?
        AND datetime < ?
        AND device_id = ?
        '''
        cursor.execute(query, (start_time, finish_time, device_id, ))
        row = cursor.fetchall()
        return row


app = Flask(__name__)
CORS(app, resources={r"/api/*": {"origins": "*"}})

@app.route('/',methods = ['GET'])
def home():
    return render_template('index.html')

@app.route('/update-gps', methods = ['POST', "GET"])
def get_gps_data():
# var msg0 = { payload: msg.payload.api_key };
# var msg1 = { payload: msg.payload.sensor };
# var msg2 = { payload: msg.payload.lat };
# var msg3 = { payload: msg.payload.lng };
# var msg4 = { payload: msg.payload.speed };
# var msg5 = { payload: msg.payload.sat };
# var msg6 = { payload: msg.payload.alt };
# var msg7 = { payload: msg.payload.date };
# var msg8 = { payload: msg.payload.time };

    info_json = request.get_json()
    print(info_json)
    api_key = info_json["api_key"]
    sensor = info_json["sensor"]
    lat = info_json["lat"]
    lng = info_json["lng"]
    speed = info_json["speed"]
    sat = info_json["sat"]
    alt = info_json["alt"]
    date = info_json["date"]
    time = info_json["time"]
    if(float(lat) != 0 and float(lng) != 0):
        update_location(api_key, lat, lng, speed, sat, alt, date, time)
    response_body = {"message": "OK"}
    response = make_response(jsonify(response_body), 200)
    return response

@app.route('/update-mpu', methods = ['POST', "GET"])
def get_mcu_data():
    info_json = request.get_json()
    # print(info_json)
# var msg0 = { payload: msg.payload.api_key };
# var msg1 = { payload: msg.payload.sensor };
# var msg2 = { payload: msg.payload.accx };
# var msg3 = { payload: msg.payload.accy };
# var msg4 = { payload: msg.payload.accz };
# var msg5 = { payload: msg.payload.gyrox };
# var msg6 = { payload: msg.payload.gyroy };
# var msg7 = { payload: msg.payload.gyroz };
# var msg8 = { payload: msg.payload.temperature };
    api_key = info_json["api_key"]
    sensor = info_json["sensor"]
    accx = info_json["accx"]
    accy = info_json["accy"]
    accz = info_json["accz"]
    gyrox = info_json["gyrox"]
    gyroy = info_json["gyroy"]
    gyroz = info_json["gyroz"]
    temperature = info_json["temperature"]

    response_body = {"message": "OK"}
    response = make_response(jsonify(response_body), 200)
    return response

@app.route('/login', methods=['POST'])
def login():
    # Nhận dữ liệu từ client
    email = request.json.get('email')
    password = request.json.get('password')

    print(email, password)
    random_cookie = secrets.token_hex(16)


    # insert_user(email=email, password=password, cookie=random_cookie)
    try:
        update_cookie(email=email, cookie=random_cookie)
        name = get_info(email, random_cookie)[0]
        response_body = {"message": "OK", "cookie": random_cookie, "name": name}
    except:
        response_body = {"message": "FAIL", "cookie": "000000", "name":"0000000"}


    print(response_body)

    response = make_response(jsonify(response_body), 200)
    return response

@app.route('/logout', methods=['POST'])
def logout():
    # Nhận dữ liệu từ client
    email = request.json.get('email')
    cookie = request.json.get('cookie')

    # try:
    update_logout(email=email, cookie=cookie)
    response_body = {"message": "OK"}
    # except(Exception e):
    #     print
    #     response_body = {"message": "FAIL"}

    print(response_body)

    response = make_response(jsonify(response_body), 200)
    return response

@app.route('/check_cookie', methods=['POST'])
def check_cookie():
    email = request.json.get('email')
    cookie = request.json.get('cookie')

    print(cookie, email)

    check = email_name_exists(email=email, cookie=cookie)

    if check:
        response_body = {"message": "OK"}
    else:   
        response_body = {"message": "FAIL"}
    print(response_body)
    response = make_response(jsonify(response_body), 200)
    return response

@app.route('/get_current_location', methods=['POST'])
def get_current_location():
    email = request.json.get('email')
    cookie = request.json.get('cookie')
    device_id = request.json.get('device_id')
    device_id = "tPmAT5Ab3j7F9"

    data = get_last_location(device_id)


    response_body = {"message": "OK", "lat": data[1], "lng" : data[2]}

    print(response_body)

    response = make_response(jsonify(response_body), 200)
    return response


@app.route('/get_location_history_list', methods=['POST'])
def get_location_history_list():
    email = request.json.get('email')
    cookie = request.json.get('cookie')
    device_id = request.json.get('device_id')
    start_time = request.json.get('startTime')
    finish_time = request.json.get('finishTime')
    device_id = "tPmAT5Ab3j7F9"

    print(request.json)

    data = get_location_list(cookie=cookie, device_id=device_id, email=email, finish_time=finish_time, start_time=start_time)
    results = [tuple(row) for row in data]
    response_body = {"message": "OK", "location": results}
    print(response_body)

    response = make_response(jsonify(response_body), 200)
    return response

def get_image():
    while True:
        try:
            with open("instance/image.jpg", "rb") as f:
                image_bytes = f.read()
            image = Image.open(BytesIO(image_bytes))
            img_io = BytesIO()
            image.save(img_io, 'JPEG')
            img_io.seek(0)
            img_bytes = img_io.read()
            yield (b'--frame\r\n'
                   b'Content-Type: image/jpeg\r\n\r\n' + img_bytes + b'\r\n')

        except Exception as e:
            print("encountered an exception: ")
            print(e)

            with open("instance/image.jpg", "rb") as f:
                image_bytes = f.read()
            image = Image.open(BytesIO(image_bytes))
            img_io = BytesIO()
            image.save(img_io, 'JPEG')
            img_io.seek(0)
            img_bytes = img_io.read()
            yield (b'--frame\r\n'
                   b'Content-Type: image/jpeg\r\n\r\n' + img_bytes + b'\r\n')
            continue

@app.route('/stream')
def stream():
    return Response(get_image(), mimetype='multipart/x-mixed-replace; boundary=frame')

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=1234)