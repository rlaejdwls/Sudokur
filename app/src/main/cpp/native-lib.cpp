#include <jni.h>
#include <string.h>
#include <iostream>
#include <opencv2/opencv.hpp>

using namespace cv;
using namespace std;

extern "C" {
void setLabel(Mat& image, string str, vector<Point> contour) {
    int fontface = FONT_HERSHEY_SIMPLEX;
    double scale = 0.5;
    int thickness = 1;
    int baseline = 0;

    Size text = getTextSize(str, fontface, scale, thickness, &baseline);
    Rect r = boundingRect(contour);

    Point pt(r.x + ((r.width - text.width) / 2), r.y + ((r.height + text.height) / 2));
    rectangle(image, pt + Point(0, baseline), pt + Point(text.width, -text.height), CV_RGB(200, 200, 200), CV_FILLED);
    putText(image, str, pt, fontface, scale, CV_RGB(0, 0, 0), thickness, 8);
}
JNIEXPORT void JNICALL
Java_kr_co_treegames_sudokur_task_camera_CameraActivity_ConvertRGBtoGray(
        JNIEnv *env, jobject instance, jlong matAddrInput, jlong matAddrResult) {
    Mat &matInput = *(Mat *) matAddrInput;
    Mat &matResult = *(Mat *) matAddrResult;
    cvtColor(matInput, matResult, CV_RGBA2GRAY);
}
JNIEXPORT jstring JNICALL
Java_kr_co_treegames_sudokur_task_main_MainPresenter_welcome(
        JNIEnv *env, jobject instance) {
    std::string welcome = "Welcome to JNI sample project";
    return env->NewStringUTF(welcome.c_str());
}
JNIEXPORT jboolean JNICALL
Java_kr_co_treegames_sudokur_task_main_MainPresenter_detect(
        JNIEnv *env, jobject instance, jlong matAddrInput, jlong matAddrResult) {
    Mat &img_input = *(Mat *) matAddrInput;
    Mat &img_result = *(Mat *) matAddrResult;
    Mat img_gray;

    if (img_input.empty()) {
        return JNI_FALSE;
    }

    //그레이스케일 이미지로 변환
    cvtColor(img_input, img_gray, COLOR_BGR2GRAY);

    //이진화 이미지로 변환
    Mat binary_image;
    threshold(img_gray, img_gray, 125, 255, THRESH_BINARY_INV | THRESH_OTSU);

    //contour를 찾는다.
    vector<vector<Point> > contours;
    findContours(img_gray, contours, RETR_LIST, CHAIN_APPROX_SIMPLE);

    //contour를 근사화한다.
    vector<Point2f> approx;
    img_result = img_input.clone();

    for (size_t i = 0; i < contours.size(); i++) {
        approxPolyDP(Mat(contours[i]), approx, arcLength(Mat(contours[i]), true)*0.02, true);

        if (fabs(contourArea(Mat(approx))) > 100) {//면적이 일정크기 이상이어야 한다.
            int size = approx.size();

            //Contour를 근사화한 직선을 그린다.
            if (size % 2 == 0) {
                line(img_result, approx[0], approx[approx.size() - 1], Scalar(0, 255, 0), 3);

                for (int k = 0; k < size - 1; k++) {
                    line(img_result, approx[k], approx[k + 1], Scalar(0, 255, 0), 3);
                }
                for (int k = 0; k < size; k++) {
                    circle(img_result, approx[k], 3, Scalar(0, 0, 255));
                }
            } else {
                line(img_result, approx[0], approx[approx.size() - 1], Scalar(0, 255, 0), 3);

                for (int k = 0; k < size - 1; k++) {
                    line(img_result, approx[k], approx[k + 1], Scalar(0, 255, 0), 3);
                }
                for (int k = 0; k < size; k++) {
                    circle(img_result, approx[k], 3, Scalar(0, 0, 255));
                }
            }

            //도형을 판정한다.
            if (size == 3) {
                setLabel(img_result, "triangle", contours[i]); //삼각형
            } else if (size == 4 && isContourConvex(Mat(approx))) {
                setLabel(img_result, "rectangle", contours[i]); //사각형
            } else if (size == 5 && isContourConvex(Mat(approx))) {
                setLabel(img_result, "pentagon", contours[i]); //오각형
            } else if (size == 6 && isContourConvex(Mat(approx))) {
                setLabel(img_result, "hexagon", contours[i]);  //육각형
            } else if (size == 10 && isContourConvex(Mat(approx))) {
                setLabel(img_result, "decagon", contours[i]);    //십각형
            } else {
                setLabel(img_result, to_string(approx.size()), contours[i]);
            }
        }
    }
    return JNI_TRUE;
}
}