package tree;

import tree.file.FileInfo;
import tree.file.Files;

public class BSTTest {
    public static void main(String[] args) {

        BST<Integer> avlTee = new BST<>();

        int[] arr = {56, 55, 32, 65, 41, 19, 68, 52, 90, 88, 83, 81, 70, 40, 35, 57};

        for (int i = 0; i < arr.length; i++) {
            avlTee.add(arr[i]);
        }

//        BinaryTrees.println(avlTee);
        System.out.println();
//D:\2020毕设\毕业设计\ProbjectDemo
        FileInfo info = Files.read("D:\\2020毕设\\毕业设计\\ProbjectDemo", new String[]{"java"});
        System.out.println("================== 统计结果[后端] =============================");
        System.out.println("java类: " + info.getFiles());
        System.out.println("代码行数: " + info.getLines());
        System.out.println("单词个数: " + info.words().length);
        System.out.println("================== 统计结果 =============================");
//  C:\\Users\\coder\\Desktop\\chatroom\\src"
        info = Files.read("D:\\2020毕设\\毕业设计\\ProbjectDemo", new String[]{"vue"});
        System.out.println();
        System.out.println("================== 统计结果[前端] =============================");
        System.out.println("vue文件个数: " + info.getFiles());
        System.out.println("代码行数: " + info.getLines());
        System.out.println("单词个数: " + info.words().length);
        System.out.println("================== 统计结果 =============================");

    }
}
