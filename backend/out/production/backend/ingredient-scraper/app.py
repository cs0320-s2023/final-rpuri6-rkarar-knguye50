import requests
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service as ChromeService
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
from flask_cors import CORS
import json
from bs4 import BeautifulSoup 
from flask import Flask, jsonify

app = Flask(__name__)
CORS(app)

@app.route('/ingredients')
def get_ingredients():
    # URLs for scraping
    TJURL = 'https://www.traderjoes.com/home/products/category?filters=%7B%22areNewProducts%22%3Atrue%7D'
    CVSURL = "https://www.cvs.com/shop/merch/new/q/Grocery/c1?widgetID=ojdve0je&mc=cat2000008&icid=shop-new-arrivals-cat7-grocery"

    # Driver initializing
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()))

    def tj(link):
        # Get URLs
        driver.get(link)
        # Wait for button clickable
        wait = WebDriverWait(driver, 10)
        wait.until(EC.element_to_be_clickable((By.CLASS_NAME, "ProductCard_card__info__2M2Ao")))
        # Get names of new products
        products =  driver.find_elements(By.CLASS_NAME, "ProductCard_card__info__2M2Ao")
        pList = []
        for product in products:
            titles = product.find_elements(By.CLASS_NAME, "ProductCard_card__title__301JH")
            for element in titles:
                pList.append(element.text)
        if len(pList)==0:
            print("pList is empty")
        return pList

    def cvs(link):
        # Get URLs
        driver.get(link)
        sList = []
        # Get all titles
        titles = driver.find_elements(By.CLASS_NAME, "css-cens5h")
        # Get all text from titles
        for element in titles:
            sList.append(element.text)
        if len(sList)==0:
            print("sList is empty")
        return sList
    fList = cvs(CVSURL) + tj(TJURL)
    response = {
        "scraped": "cvs-tdjs",
        "result":fList
    }
    jsonify(response)
    return response
if __name__ == '__main__':
    app.run(debug=True)
    