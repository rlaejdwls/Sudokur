package kr.co.treegames.core.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Hwang on 2016-11-01
 * 작성자 : 황의택
 * 내용 : gson을 이용한 json 파일 직렬화 클래스
 */
public class JSONSerializer {
    private static Gson gson;

    /**
     * path에 지정된 파일로부터 json을 읽어들여 klass형으로 변환하여 반환하는 함수
     * @param path json 파일 경로
     * @param klass json이 변환될 클래스의 type
     * @param <T> json에서 변환되는 클래스 type
     * @return T
     */
    public static <T> T read(String path, Class<T> klass) {
        if (gson == null) {
            gson = new Gson();
        }

        BufferedReader reader = null;
        try {
            if (new File(path).exists()) {
                reader = new BufferedReader(new FileReader(path));
                return gson.fromJson(reader, klass);
            } else {
                throw new FileNotFoundException("File not found : " + path);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (JsonSyntaxException jse) {
            jse.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
    /**
     * path에 지정된 경로로 T 클래스를 json으로 출력하는 함수
     * @param path 출력하는 파일의 경로
     * @param model json으로 출력하려는 모델
     * @param <T> 출력하려는 클래스의 type
     */
    public static <T> boolean write(String path, T model) {
        return write(path, model, false);
    }
    /**
     * path에 지정된 경로로 T 클래스를 json으로 출력하는 함수
     * @param path 출력하는 파일의 경로
     * @param model json으로 출력하려는 모델
     * @param append 기존 내용에 추가하는지 여부 true:추가, false:덮어쓰기
     * @param <T>
     * @return
     */
    public static <T> boolean write(String path, T model, boolean append) {
        if (gson == null) {
            gson = new Gson();
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(path, append));
            gson.toJson(model, writer);
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
    /**
     * JSON to Object
     * @param json
     * @param klass
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Class<T> klass) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.fromJson(json, klass);
    }
    /**
     * Object to JSON
     * @param model
     * @param <T>
     * @return
     */
    public static <T> String toJSON(T model) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.toJson(model);
    }
}
