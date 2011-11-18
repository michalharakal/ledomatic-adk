package com.ledomatic.adk.utils;

public class HSV2RGB {
	 private HSV2RGB() {
	    }

	    /**
	     * Converts a color from HSV to RGB
	     * @param hsv
	     * @return
	     */
	    static public RGB convert(HSV hsv) {
	        float h;
	        float f, p, q, t;
	        int i;
	        RGB ret = new RGB();

	        h = hsv.getH();
	        if (hsv.getS() == 0) {
	            ret.setR(0);
	            ret.setB(0);
	            ret.setG(0);
	        } else {
	            if (h == 360)
	                h = 0;
	            h /= 60.0;
	            i = (int) h;
	            f = h - i;
	            p = hsv.getV() * (1 - hsv.getS());
	            q = hsv.getV() * (1 - (hsv.getS() * f));
	            t = hsv.getV() * (1 - hsv.getS() * (1 - f));
	            switch (i) {
	                case 0:
	                    ret.setR(hsv.getV());
	                    ret.setG(t);
	                    ret.setB(p);
	                    break;
	                case 1:
	                    ret.setR(q);
	                    ret.setG(hsv.getV());
	                    ret.setB(p);
	                    break;
	                case 2:
	                    ret.setR(p);
	                    ret.setG(hsv.getV());
	                    ret.setB(t);
	                    break;
	                case 3:
	                    ret.setR(p);
	                    ret.setG(q);
	                    ret.setB(hsv.getV());
	                    break;
	                case 4:
	                    ret.setR(t);
	                    ret.setG(p);
	                    ret.setB(hsv.getV());
	                    break;
	                case 5:
	                    ret.setR(hsv.getV());
	                    ret.setG(p);
	                    ret.setB(q);
	                    break;
	                default:
	                    ret.setR(1);
	                    ret.setG(1);
	                    ret.setB(1);
	                    break;
	            }
	        }
	        return ret;
	    }

}
