from PIL import Image
import glob, os

size = 400, 400

for infile in glob.glob("*.JPG"):
    file, ext = os.path.splitext(infile)
    im = Image.open(infile)
    im.thumbnail(size, Image.ANTIALIAS)
    im.save(file + "_.jpg", "JPEG")