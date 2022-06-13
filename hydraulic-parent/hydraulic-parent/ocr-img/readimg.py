#coding=utf-8
from PIL import Image
import pytesseract
img=Image.open('/Users/tangcaijun/Desktop/test.png')
print("====img====")
newimg = img.convert('L')  #图片灰度处理
print(pytesseract.image_to_string(newimg))#默认识别英文和数字