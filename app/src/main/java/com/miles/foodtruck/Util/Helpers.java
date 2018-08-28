package com.miles.foodtruck.Util;



public class Helpers {

    public static int[]  extractDateTime(String type, String str){

        switch (type)
        {

            case Constant.DatePicker:
                String[] output = str.split("/");

                int[] dates = {Integer.parseInt(output[0]),Integer.parseInt(output[1]),Integer.parseInt(output[2])};

                return dates;

            case Constant.TimePicker:

                int hours;
                int minutes;

                if (str.substring(str.length()-2,str.length()) .equals("AM"))
                {
                    hours = Integer.parseInt(str.substring(0,1));
                }
                else
                    {
                        if (Integer.parseInt(str.substring(0,1)) == 12)
                        {
                            hours = 12;
                        }
                        else
                            {
                                hours  = Integer.parseInt(str.substring(0,1)) + 12;
                            }

                    }

                minutes = Integer.parseInt(str.substring(3,4));
                int[] time = {hours, minutes};
                return time;

        }


        return null;
    }





}
