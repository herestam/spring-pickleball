import requests

AUTH_SERVICE = "http://localhost:8081/internal/auth/validate"
API_KEY = "9f3c7e2a-6b11-4d4c-a2a0-8c51d91e7b52"

token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMTExIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE3NjkyNDkxNjUsImV4cCI6MTc2OTI1MDA2NX0.hK4ekm-jdGp2tTua0lQ-6gjsHiqapjSbSXw7gXlJn19P3P-BWpiVwss2PR0PWXeAJMkHrsoStr-xZTZ3OzcGtg"  # JWT from user

headers = {
    "Authorization": f"Bearer {token}",
    "X-API-KEY": API_KEY,
    "Content-Type": "application/json"
}

try:
    response = requests.post(AUTH_SERVICE, headers=headers, timeout=5)
    print("Status:", response.status_code)
    print("Response:", response.json())
except Exception as e:
    print("Error:", e)
