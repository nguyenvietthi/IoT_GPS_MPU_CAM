from datetime import datetime, timedelta, timezone

def is_current_time_greater_than(given_time_str):
    gmt_plus_7 = timezone(timedelta(hours=7))
    current_time_gmt_plus_7 = datetime.now(gmt_plus_7)
    given_time = datetime.strptime(given_time_str, '%Y-%m-%d %H:%M:%S')
    given_time = given_time.replace(tzinfo=gmt_plus_7)
    return (current_time_gmt_plus_7 - given_time).total_seconds() < 5

# Ví dụ sử dụng hàm
test_time_str = '2024-06-09 21:53:00'
print(is_current_time_greater_than(test_time_str))