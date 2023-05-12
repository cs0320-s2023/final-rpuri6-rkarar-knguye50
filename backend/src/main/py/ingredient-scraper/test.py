from app import app
import unittest

class FlaskTest(unittest.TestCase):
    def test_status(self):
        test_object = app.test_client(self)
        response = test_object.get("/ingredients")
        status_code = response.status_code
        self.assertEqual(status_code, 200)

    def test_response_type(self):
        test_object = app.test_client(self)
        response = test_object.get("/ingredients")
        resp_type = response.content_type
        self.assertEqual(resp_type, "application/json")
    
    def test_result_and_sites(self):
        test_object = app.test_client(self)
        response = test_object.get("/ingredients")
        resp_data = response.data.decode()
        self.assertTrue("result" in resp_data)
        self.assertTrue("cvs-tdjs" in resp_data)
if __name__ == "__main__":
    unittest.main()