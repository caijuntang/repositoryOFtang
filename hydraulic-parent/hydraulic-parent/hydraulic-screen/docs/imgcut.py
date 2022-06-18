#coding=utf-8
from PIL import ImageGrab
import time
import logging
 
 
def Psc():
    # 自增序号，但若程序重启，会覆盖之前的文件
    while True:
        time_start=time.time()
        #外河水位截图保存 1021, 130,1060,145  原生坐标 1025, 100, 1070, 119
        im1 = ImageGrab.grab(bbox=(1008, 150,1108,180 ))
        im1.save('D:\\waterline_ocr\\outsideVal.png')
        #内河水位截图保存 492,645,531,633   原生坐标 453, 657, 504, 674
        im2 = ImageGrab.grab(bbox=(280,929,380,956))
        im2.save('D:\\waterline_ocr\\insideVal.png')
        #前池水位截图保存 492,645,531,633    原生坐标 453, 657, 504, 674
        im3 = ImageGrab.grab(bbox=(438,715,528,735))
        im3.save('D:\\waterline_ocr\\foreVal.png')
        time_end=time.time()
        time_c=time_end-time_start
        logging.info('time cost:',time_c,'s')
        print('time cost:',time_c,'s')
        break
if __name__ == '__main__':
    Psc()