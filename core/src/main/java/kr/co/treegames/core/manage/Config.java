package kr.co.treegames.core.manage;

import kr.co.treegames.core.util.JSONSerializer;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import kr.co.treegames.core.util.JSONSerializer;

/**
 * Created by Hwang on 2016-11-02.
 * 작성자 : 황의택
 * 내용 : 앱의 환경 설정을 저장하는 클래스
 */
public class Config {
    private HashMap<String, Object> config;
    private String path;

    public Config(String path) {
        if (!new File(path).exists()) {
            config = new HashMap();
            JSONSerializer.write(path, config);
        }
        config = JSONSerializer.read(path, HashMap.class);

        this.path = path;
    }

    public Iterator getIterator() {
        return config.keySet().iterator();
    }

    /**
     * @param key
     * @param value
     */
    public Config set(String key, Object value) {
        config.put(key, value);
        return this;
    }
    /**
     * @param key
     * @param defObject
     * @return
     */
    public Object get(String key, Object defObject) {
        if (config.containsKey(key)) {
            return config.get(key);
        } else {
            return defObject;
        }
    }
    /**
     * @param key
     * @param defValue
     * @return
     */
    public String getString(String key, String defValue) {
        if (config.containsKey(key)) {
            if (config.get(key) != null) {
                return config.get(key).toString();
            } else {
                return defValue;
            }
        } else {
            return defValue;
        }
    }
    /**
     * //!issue gson에서 map을 출력 할때 int 타입도 float으로 바꿔 출력하는 문제가 있음
     *          현재 임시로 float형까지 모두 int로 변환하여 반환함
     *          해결방안으로는 GsonBuilder의 Adapter를 정의하여 타입 판단을 해줄 필요성이 있음
     *          시간이 촉박함으로 여유 있을 때 수정
     *          https://github.com/google/gson/issues/45
     *          상위 주소 중간쯤 보면 아답터 예제 부분이 있는데 도움이 되려나 마려나..
     * @param key
     * @param defValue
     * @return
     */
    public int getInt(String key, int defValue) {
        if (config.containsKey(key)) {
            if (config.get(key) instanceof Integer ||
                    config.get(key) instanceof Float) {
                return (int) config.get(key);
            } else {
                return defValue;
            }
        } else {
            return defValue;
        }
    }
    /**
     * @param key
     * @param defValue
     * @return
     */
    public boolean getBoolean(String key, boolean defValue) {
        if (config.containsKey(key)) {
            if (config.get(key) instanceof Boolean) {
                return (boolean) config.get(key);
            } else {
                return defValue;
            }
        } else {
            return defValue;
        }
    }
    /**
     * key에 해당하는 설정 삭제
     * @param key
     */
    public void remove(String key) {
        config.remove(key);
    }
    /**
     * 환경설정 초기화
     */
    public void clear() {
        config.clear();
    }
    /**
     * 환경설정을 commit하기 전으로 되돌리는 함수
     * 파일을 삭제 했을 경우 되돌려지지 않는다
     * @return 성공:true, 실패:false
     */
    public boolean rollback() {
        if (new File(path).exists()) {
            config = JSONSerializer.read(path, HashMap.class);
            if (config == null) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    /**
     * 보존되어 있는 config 파일을 삭제하는 함수
     * @return
     */
    public boolean delete() {
        return delete(path);
    }
    /**
     * path에 있는 파일을 삭제하는 함수
     * @param path
     * @return
     */
    public boolean delete(String path) {
        return new File(path).delete();
    }
    /**
     * config를 path에 파일로 저장하는 함수
     * @param path
     */
    public boolean save(String path) {
        return JSONSerializer.write(path, config);
    }
    /**
     * config를 보존하는 함수
     * @return
     */
    public boolean commit() {
        return JSONSerializer.write(this.path, config);
    }
}
