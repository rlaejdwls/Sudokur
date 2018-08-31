package kr.co.treegames.core.manage;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * Created by Hwang on 2016-11-08.
 * 작성자 : 황의택
 * 내용 : 나열되어 있는 명령어(Activity, Method 등..)를 순차적으로 실행시켜주는 클래스
 */
public class Navigator {
    public static final String TYPE_ACTIVITY = "ACTIVITY";
    public static final String TYPE_METHOD = "METHOD";
    public static final String TYPE_SKIP = "SKIP";

    private static Navigator navigator;
    private String navi = "[]";

    private Navigator() {
    }

    public synchronized static Navigator getInstance() {
        if (navigator == null) {
            navigator = new Navigator();
        }
        return navigator;
    }

    public static Navigator set(String navi) {
        Navigator.getInstance().navi = navi;
        return navigator;
    }

    public static void navigate(Object object/*, String navi*/) {
        try {
            JSONArray naviArray = new JSONArray(Navigator.getInstance().navi);
            if (naviArray.length() == 0) return;

            if (naviArray != null) {
                JSONObject naviObj = (JSONObject) naviArray.get(0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    naviArray.remove(0);
                    Navigator.getInstance().navi = naviArray.toString();
                } else {
                    String result = "[";
                    for (int i = 1; i < naviArray.length(); i++) {
                        result += naviArray.get(i).toString();
                        if (i != (naviArray.length() - 1)) result += ", ";
                    }
                    result += "]";
                    Navigator.getInstance().navi = result;
                }
                if (object instanceof Activity) {
                    next((Activity) object, naviObj);
                } else if (object instanceof Fragment){
                    next((Fragment) object, naviObj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void next(Activity activity, JSONObject navi) {
        try {
            Logger.d(navi.toString());

            switch (navi.get("type").toString()) {
                case TYPE_ACTIVITY:
                    Class<?> klassActivity = Class.forName(activity.getApplicationInfo()
                            .packageName + "." + navi.get("next").toString());
                    Intent intent = new Intent(activity, klassActivity);

                    if (navi.has("params")) {
                        JSONObject params = new JSONObject(navi.get("params").toString());
                        Iterator<String> iterator = params.keys();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            Object param = params.get(key);
                            if (param instanceof String) {
                                intent.putExtra(key, (String) param);
                            } else if (param instanceof Integer) {
                                intent.putExtra(key, (int) param);
                            } else if (param instanceof Long) {
                                intent.putExtra(key, (long) param);
                            } else if (param instanceof Boolean) {
                                intent.putExtra(key, (boolean) param);
                            } else if (param instanceof Float) {
                                intent.putExtra(key, (float) param);
                            } else if (param instanceof Double) {
                                intent.putExtra(key, (double) param);
                            }
                        }
                    }

                    activity.startActivity(intent);
                    break;
                case TYPE_METHOD:
                    try {
                        Object instance;
                        if (navi.get("instance") != null && !navi.get("instance").equals("this")) {
                            Field field = activity.getClass().getDeclaredField(navi.get("instance").toString());
                            field.setAccessible(true);
                            instance = field.get(activity);
                        } else {
                            instance = activity;
                        }

                        Class<?> klass = instance.getClass();
                        Object[] objects = null;
                        Class<?>[] types = null;
                        if (navi.has("params")) {
                            JSONArray params = new JSONArray(navi.get("params").toString());
                            objects = new Object[params.length()];
                            types = new Class<?>[params.length()];

                            for (int i = 0; i < params.length(); i++) {
                                objects[i] = params.get(i);
                                if (objects[i] instanceof Integer) {
                                    types[i] = int.class;
                                } else if (objects[i] instanceof Long) {
                                    types[i] = long.class;
                                } else if (objects[i] instanceof Float) {
                                    types[i] = float.class;
                                } else if (objects[i] instanceof Double) {
                                    types[i] = double.class;
                                } else if (objects[i] instanceof Boolean) {
                                    types[i] = boolean.class;
                                } else if (objects[i] instanceof Byte) {
                                    types[i] = byte.class;
                                } else if (objects[i] instanceof Short) {
                                    types[i] = short.class;
                                } else {
                                    types[i] = params.get(i).getClass();
                                }
                            }
                        }

                        Method method = klass.getMethod(navi.get("name").toString(), types);
                        method.invoke(instance, objects);
                        if (!navi.get("name").toString().equals("finish")) {
                            navigate(activity);
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    throw new RuntimeException("Not found navi type:" + navi.get("type").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void next(Fragment fragment, JSONObject navi) {
        try {
            switch (navi.get("type").toString()) {
                case TYPE_ACTIVITY:
                    Class<?> klassActivity = Class.forName(fragment.getActivity().getApplicationInfo()
                            .packageName + "." + navi.get("next").toString());
                    Intent intent = new Intent(fragment.getActivity(), klassActivity);

                    if (navi.has("params")) {
                        JSONObject params = new JSONObject(navi.get("params").toString());
                        Iterator<String> iterator = params.keys();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            Object param = params.get(key);
                            if (param instanceof String) {
                                intent.putExtra(key, (String) param);
                            } else if (param instanceof Integer) {
                                intent.putExtra(key, (int) param);
                            } else if (param instanceof Long) {
                                intent.putExtra(key, (long) param);
                            } else if (param instanceof Boolean) {
                                intent.putExtra(key, (boolean) param);
                            } else if (param instanceof Float) {
                                intent.putExtra(key, (float) param);
                            } else if (param instanceof Double) {
                                intent.putExtra(key, (double) param);
                            }
                        }
                    }

                    fragment.startActivity(intent);
                    break;
                case TYPE_METHOD:
                    try {
                        Object instance;
                        if (navi.get("instance") != null && !navi.get("instance").equals("this")) {
                            Field field = fragment.getClass().getDeclaredField(navi.get("instance").toString());
                            field.setAccessible(true);
                            instance = field.get(fragment);
                        } else {
                            instance = fragment;
                        }

                        Class<?> klass = instance.getClass();
                        Object[] objects = null;
                        Class<?>[] types = null;
                        if (navi.has("params")) {
                            JSONArray params = new JSONArray(navi.get("params").toString());
                            objects = new Object[params.length()];
                            types = new Class<?>[params.length()];

                            for (int i = 0; i < params.length(); i++) {
                                objects[i] = params.get(i);
                                if (objects[i] instanceof Integer) {
                                    types[i] = int.class;
                                } else if (objects[i] instanceof Long) {
                                    types[i] = long.class;
                                } else if (objects[i] instanceof Float) {
                                    types[i] = float.class;
                                } else if (objects[i] instanceof Double) {
                                    types[i] = double.class;
                                } else if (objects[i] instanceof Boolean) {
                                    types[i] = boolean.class;
                                } else if (objects[i] instanceof Byte) {
                                    types[i] = byte.class;
                                } else if (objects[i] instanceof Short) {
                                    types[i] = short.class;
                                } else {
                                    types[i] = params.get(i).getClass();
                                }
                            }
                        }

                        Method method = klass.getMethod(navi.get("name").toString(), types);
                        method.invoke(instance, objects);
                        if (!navi.get("name").toString().equals("finish") &&
                                (!navi.has("new_page") || !Boolean.parseBoolean(navi.get("new_page").toString()))) {
                            navigate(fragment);
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    break;
                case TYPE_SKIP:
                    break;
                default:
                    throw new RuntimeException("Not found navi type:" + navi.get("type").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}