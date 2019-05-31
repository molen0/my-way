/*
 * Copyright (c) 2017-2018.  放牛极客<l_iupeiyu@qq.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * </p>
 *
 */

package adinnet.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * UUID工具类
 * author wangren
 * date 2018/09/17
 */
public class UuidUtil {
    public UuidUtil(){

    }
    /**
     * 获得一个UUID
     *
     * @return String UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 获得指定数目的UUID
     *
     * @param number int 需要获得的UUID数量
     * @return String[] UUID数组
     */
    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = getUUID();
        }
        return ss;
    }

    public static String createRandomNo( ){
        //组成： mch_id+yyyymmdd+10位一天内不能重复的数字
        //10位一天内不能重复的数字实现方法如下:
        Date dt=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowTime= df.format(dt);
        return  nowTime + getRandomNum(10);
    }

    /**
     * 生成特定位数的随机数字
     * @param length
     * @return
     */
    public static String getRandomNum(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }

    public static void main(String[] args) {
        System.out.println("======= "+createRandomNo());
    }
}
