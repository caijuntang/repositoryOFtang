#coding=utf-8
from PIL import Image
import pytesseract
import requests
import time
import logging
import json

logging.info("===================开始识别=================")
time_start=time.time()
try:
    outsideVal = pytesseract.image_to_string(Image.open('D:\\waterline_ocr\\outsideVal.png'))
    insideVal = pytesseract.image_to_string(Image.open('D:\\waterline_ocr\\insideVal.png'))
    foreVal = pytesseract.image_to_string(Image.open('D:\\waterline_ocr\\foreVal.png'))
    print(outsideVal,insideVal,foreVal)
    ov=str(outsideVal).strip()
    iv=str(insideVal).strip()
    fv=str(foreVal).strip()
    urlStr='http://192.168.2.8:8090/screen/openApi/reportWaterLine?outsideVal='+ov+'&insideVal='+iv+'&foreVal='+fv
    time_end=time.time()
    time_c=time_end-time_start
    print("time cost:"+str(time_c)+"==================请求链接："+urlStr)
    logging.info("time cost"+str(time_c)+"==================请求链接："+urlStr)
    r = requests.get(urlStr)
    response='返回结果：code='+str(r.status_code)+'内容：'+json.dumps(r.json())
    print(response)
    logging.info(response)
except Exception as e:
        print(e)
        logging.error(e)