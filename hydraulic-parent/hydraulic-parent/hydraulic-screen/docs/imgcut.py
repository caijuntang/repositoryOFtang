#coding=utf-8
from PIL import ImageGrab
import time
import logging
 
 
def Psc():
    # ������ţ����������������Ḳ��֮ǰ���ļ�
    while True:
        time_start=time.time()
        #���ˮλ��ͼ���� 1021, 130,1060,145  ԭ������ 1025, 100, 1070, 119
        im1 = ImageGrab.grab(bbox=(1008, 150,1108,180 ))
        im1.save('D:\\waterline_ocr\\outsideVal.png')
        #�ں�ˮλ��ͼ���� 492,645,531,633   ԭ������ 453, 657, 504, 674
        im2 = ImageGrab.grab(bbox=(280,929,380,956))
        im2.save('D:\\waterline_ocr\\insideVal.png')
        #ǰ��ˮλ��ͼ���� 492,645,531,633    ԭ������ 453, 657, 504, 674
        im3 = ImageGrab.grab(bbox=(438,715,528,735))
        im3.save('D:\\waterline_ocr\\foreVal.png')
        time_end=time.time()
        time_c=time_end-time_start
        logging.info('time cost:',time_c,'s')
        print('time cost:',time_c,'s')
        break
if __name__ == '__main__':
    Psc()