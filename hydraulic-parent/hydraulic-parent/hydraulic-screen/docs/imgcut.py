#coding=utf-8
from PIL import ImageGrab
import time
import logging
from logging.handlers import RotatingFileHandler

logger = logging.getLogger('orcimg')
logger.setLevel(level = logging.INFO)
handler =RotatingFileHandler('D:\\waterline_ocr\\log.txt', maxBytes=1024*1024*50, backupCount=5, encoding='utf-8')
handler.setLevel(logging.INFO)
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
handler.setFormatter(formatter)
console = logging.StreamHandler()
console.setLevel(logging.DEBUG)
logger.addHandler(handler)
logger.addHandler(console)
 
def Psc():
    # 自增序号，但若程序重启，会覆盖之前的文件
    while True:
        time_start=time.time()
        #1
        im1va = ImageGrab.grab(bbox=(465, 235,536,251))
        im1vb = ImageGrab.grab(bbox=(471, 258,539,280))
        im1vc = ImageGrab.grab(bbox=(465, 283,536,300))
        im1aa = ImageGrab.grab(bbox=(465, 308,536,325))
        im1ab = ImageGrab.grab(bbox=(465, 333,536,350))
        im1ac = ImageGrab.grab(bbox=(465, 358,536,375))


        im1va.save('D:\\pumpdata_ocr\\pump1va.png')
        im1vb.save('D:\\pumpdata_ocr\\pump1vb.png')
        im1vc.save('D:\\pumpdata_ocr\\pump1vc.png')
        im1aa.save('D:\\pumpdata_ocr\\pump1aa.png')
        im1ab.save('D:\\pumpdata_ocr\\pump1ab.png')
        im1ac.save('D:\\pumpdata_ocr\\pump1ac.png')
        #2
        im2va = ImageGrab.grab(bbox=(640, 235,710,251))
        im2vb = ImageGrab.grab(bbox=(640, 257,710,275))
        im2vc = ImageGrab.grab(bbox=(640, 283,710,300))
        im2aa = ImageGrab.grab(bbox=(640, 308,710,325))
        im2ab = ImageGrab.grab(bbox=(640, 333,710,350))
        im2ac = ImageGrab.grab(bbox=(640, 358,710,375))
        
        
        im2va.save('D:\\pumpdata_ocr\\pump2va.png')
        im2vb.save('D:\\pumpdata_ocr\\pump2vb.png')
        im2vc.save('D:\\pumpdata_ocr\\pump2vc.png')
        im2aa.save('D:\\pumpdata_ocr\\pump2aa.png')
        im2ab.save('D:\\pumpdata_ocr\\pump2ab.png')
        im2ac.save('D:\\pumpdata_ocr\\pump2ac.png')
        #3
        im3va = ImageGrab.grab(bbox=(810, 235,880,251))
        im3vb = ImageGrab.grab(bbox=(810, 257,880,275))
        im3vc = ImageGrab.grab(bbox=(810, 283,880,300))
        im3aa = ImageGrab.grab(bbox=(810, 308,880,325))
        im3ab = ImageGrab.grab(bbox=(810, 333,880,350))
        im3ac = ImageGrab.grab(bbox=(810, 358,880,375))
        
        im3va.save('D:\\pumpdata_ocr\\pump3va.png')
        im3vb.save('D:\\pumpdata_ocr\\pump3vb.png')
        im3vc.save('D:\\pumpdata_ocr\\pump3vc.png')
        im3aa.save('D:\\pumpdata_ocr\\pump3aa.png')
        im3ab.save('D:\\pumpdata_ocr\\pump3ab.png')
        im3ac.save('D:\\pumpdata_ocr\\pump3ac.png')
        #4
        im4va = ImageGrab.grab(bbox=(975, 235,1050,251))
        im4vb = ImageGrab.grab(bbox=(975, 257,1050,275))
        im4vc = ImageGrab.grab(bbox=(975, 283,1050,300))
        im4aa = ImageGrab.grab(bbox=(975, 308,1050,325))
        im4ab = ImageGrab.grab(bbox=(975, 333,1050,350))
        im4ac = ImageGrab.grab(bbox=(975, 358,1050,375))
        
        im4va.save('D:\\pumpdata_ocr\\pump4va.png')
        im4vb.save('D:\\pumpdata_ocr\\pump4vb.png')
        im4vc.save('D:\\pumpdata_ocr\\pump4vc.png')
        im4aa.save('D:\\pumpdata_ocr\\pump4aa.png')
        im4ab.save('D:\\pumpdata_ocr\\pump4ab.png')
        im4ac.save('D:\\pumpdata_ocr\\pump4ac.png')
        
        time_end=time.time()
        time_c=time_end-time_start
        logger.info('time cost:'+str(time_c))
        time.sleep(10)
if __name__ == '__main__':
    Psc()