#coding=utf-8
from PIL import Image
import pytesseract
import requests
import time
from PIL import ImageGrab
import logging
from logging.handlers import RotatingFileHandler
import json

logger = logging.getLogger('orcimg')
logger.setLevel(level = logging.INFO)
handler =RotatingFileHandler('D:\\pumpData_ocr\\log.txt', maxBytes=1024*1024*50, backupCount=5, encoding='utf-8')
handler.setLevel(logging.INFO)
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
handler.setFormatter(formatter)
console = logging.StreamHandler()
console.setLevel(logging.DEBUG)
logger.addHandler(handler)
logger.addHandler(console)
logger.info("===================开始识别=================")

urlStr='http://192.168.3.4:8090/screen/openApi/reportPumpData'
headers={"Content-Type":"application-json"}

  # 自增序号，但若程序重启，会覆盖之前的文件
while True:
    try:
        time_start=time.time()
        #1
        im1va = ImageGrab.grab(bbox=(465, 235,536,251))
        im1vb = ImageGrab.grab(bbox=(474, 257,536,275))
        im1vc = ImageGrab.grab(bbox=(465, 283,536,300))
        im1aa = ImageGrab.grab(bbox=(465, 308,536,325))
        im1ab = ImageGrab.grab(bbox=(465, 333,536,350))
        im1ac = ImageGrab.grab(bbox=(465, 358,536,375))

        pump1va = pytesseract.image_to_string(im1va)
        pump1vb = pytesseract.image_to_string(im1vb)
        pump1vc = pytesseract.image_to_string(im1vc)
        pump1aa = pytesseract.image_to_string(im1aa)
        pump1ab = pytesseract.image_to_string(im1ab)
        pump1ac = pytesseract.image_to_string(im1ac)
        
        #print(pump1va)
        #print(pump1vb)
        #print(pump1vc)
        #print(pump1aa)
        #print(pump1ab)
        #print(pump1ac)
        p1va=str(pump1va).strip().replace(" ","")
        p1vb=str(pump1vb).strip().replace(" ","")
        p1vc=str(pump1vc).strip().replace(" ","")
        p1aa=str(pump1aa).strip().replace(" ","")
        p1ab=str(pump1ab).strip().replace(" ","")
        p1ac=str(pump1ac).strip().replace(" ","")
        #print(p1va)
        #print(p1vb)
        #print(p1vc)
        #print(p1aa)
        #print(p1ab)
        #print(p1ac)
        
        data1={
        "pumpNo":1,
        "va":p1va,
        "vb":p1vb,
        "vc":p1vc,
        "aa":p1aa,
        "ab":p1ab,
        "ac":p1ac}
        pumpdata1={"stationId":1,"pumpData":data1}
        print(pumpdata1)
        r1 = requests.post(urlStr,json=json.dumps(pumpdata1),headers=headers)
        logger.debug('返回结果：code='+str(r1.status_code)+'内容：'+json.dumps(r1.json()))
        #2
        im2va = ImageGrab.grab(bbox=(640, 235,710,251))
        im2vb = ImageGrab.grab(bbox=(640, 257,710,275))
        im2vc = ImageGrab.grab(bbox=(640, 283,710,300))
        im2aa = ImageGrab.grab(bbox=(640, 308,710,325))
        im2ab = ImageGrab.grab(bbox=(640, 333,710,350))
        im2ac = ImageGrab.grab(bbox=(640, 358,710,375))
        
        pump2va = pytesseract.image_to_string(im2va)
        pump2vb = pytesseract.image_to_string(im2vb)
        pump2vc = pytesseract.image_to_string(im2vc)
        pump2aa = pytesseract.image_to_string(im2aa)
        pump2ab = pytesseract.image_to_string(im2ab)
        pump2ac = pytesseract.image_to_string(im2ac)
        
        #print(pump2va)
        #print(pump2vb)
        #print(pump2vc)
        #print(pump2aa)
        #print(pump2ab)
        #print(pump2ac)
        p2va=str(pump2va).strip().replace(" ","")
        p2vb=str(pump2vb).strip().replace(" ","")
        p2vc=str(pump2vc).strip().replace(" ","")
        p2aa=str(pump2aa).strip().replace(" ","")
        p2ab=str(pump2ab).strip().replace(" ","")
        p2ac=str(pump2ac).strip().replace(" ","")
        
        #print(p2va)
        #print(p2vb)
        #print(p2vc)
        #print(p2aa)
        #print(p2ab)
        #print(p2ac)
        data2={
        "pumpNo":2,
        "va":p2va,
        "vb":p2vb,
        "vc":p2vc,
        "aa":p2aa,
        "ab":p2ab,
        "ac":p2ac}
        pumpdata2={"stationId":1,"pumpData":data2}
        r2 = requests.post(urlStr,pumpdata2,)
        logger.debug('返回结果：code='+str(r2.status_code)+'内容：'+json.dumps(r2.json()))
        #3
        im3va = ImageGrab.grab(bbox=(810, 235,880,251))
        im3vb = ImageGrab.grab(bbox=(810, 257,880,275))
        im3vc = ImageGrab.grab(bbox=(810, 283,880,300))
        im3aa = ImageGrab.grab(bbox=(810, 308,880,325))
        im3ab = ImageGrab.grab(bbox=(810, 333,880,350))
        im3ac = ImageGrab.grab(bbox=(810, 358,880,375))
        
        pump3va = pytesseract.image_to_string(im3va)
        pump3vb = pytesseract.image_to_string(im3vb)
        pump3vc = pytesseract.image_to_string(im3vc)
        pump3aa = pytesseract.image_to_string(im3aa)
        pump3ab = pytesseract.image_to_string(im3ab)
        pump3ac = pytesseract.image_to_string(im3ac)
        
        #print(pump3va)
        #print(pump3vb)
        #print(pump3vc)
        #print(pump3aa)
        #print(pump3ab)
        #print(pump3ac)
        p3va=str(pump3va).strip().replace(" ","")
        p3vb=str(pump3vb).strip().replace(" ","")
        p3vc=str(pump3vc).strip().replace(" ","")
        p3aa=str(pump3aa).strip().replace(" ","")
        p3ab=str(pump3ab).strip().replace(" ","")
        p3ac=str(pump3ac).strip().replace(" ","")
        #print(p3va)
        #print(p3vb)
        #print(p3vc)
        #print(p3aa)
        #print(p3ab)
        #print(p3ac)
        data3={
        "pumpNo":3,
        "va":p3va,
        "vb":p3vb,
        "vc":p3vc,
        "aa":p3aa,
        "ab":p3ab,
        "ac":p3ac}
        pumpdata3={"stationId":1,"pumpData":data3}
        r3 = requests.post(urlStr,pumpdata3,headers=headers)
        logger.debug('返回结果：code='+str(r3.status_code)+'内容：'+json.dumps(r3.json()))
        #4
        im4va = ImageGrab.grab(bbox=(979, 235,1050,251))
        im4vb = ImageGrab.grab(bbox=(978, 257,1050,275))
        im4vc = ImageGrab.grab(bbox=(975, 283,1050,300))
        im4aa = ImageGrab.grab(bbox=(975, 308,1050,325))
        im4ab = ImageGrab.grab(bbox=(975, 333,1050,350))
        im4ac = ImageGrab.grab(bbox=(975, 358,1050,375))
        
        pump4va = pytesseract.image_to_string(im4va)
        pump4vb = pytesseract.image_to_string(im4vb)
        pump4vc = pytesseract.image_to_string(im4vc)
        pump4aa = pytesseract.image_to_string(im4aa)
        pump4ab = pytesseract.image_to_string(im4ab)
        pump4ac = pytesseract.image_to_string(im4ac)
        #print(pump4va)
        #print(pump4vb)
        #print(pump4vc)
        #print(pump4aa)
        #print(pump4ab)
        #print(pump4ac)
        p4va=str(pump4va).strip().replace(" ","")
        p4vb=str(pump4vb).strip().replace(" ","")
        p4vc=str(pump4vc).strip().replace(" ","")
        p4aa=str(pump4aa).strip().replace(" ","")
        p4ab=str(pump4ab).strip().replace(" ","")
        p4ac=str(pump4ac).strip().replace(" ","")
        #print(p4va)
        #print(p4vb)
        #print(p4vc)
        #print(p4aa)
        #print(p4ab)
        #print(p4ac)
        data4={
        "pumpNo":4,
        "va":p4va,
        "vb":p4vb,
        "vc":p4vc,
        "aa":p4aa,
        "ab":p4ab,
        "ac":p4ac}
        
        pumpdata4={"stationId":1,"pumpData":data4}
        r4 = requests.post(urlStr,pumpdata4)
        logger.debug('返回结果：code='+str(r4.status_code)+'内容：'+json.dumps(r4.json()))
        time_end=time.time()
        time_c=time_end-time_start
        logger.info("time cost"+str(time_c))
    except Exception as e:
        logger.error(e)