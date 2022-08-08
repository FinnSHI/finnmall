package com.finn.gulimall.thirdparty.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.finn.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

/*
 * @description: OSS
 * @author: Finn
 * @create: 2022/04/29 17:30
 */
@RestController
public class OssController {

    @Autowired
    OSS ossClient;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    String endpoint;
    @Value("${spring.cloud.alicloud.oss.bucket}")
    String bucket;
    @Value("${spring.cloud.alicloud.access-key}")
    String accessKeyId;

//    @RequestMapping("/oss/test")
//    public R test(){
//        return R.ok().put("data", "ok");
//    }

    @RequestMapping("/oss/policy")
    public R policy() {
        String host = "https://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint
//        String host = bucket + "." + endpoint; // host的格式为 bucketname.endpoint
        // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
        //String callbackUrl = "http://88.88.88.88:8888";

        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dir = format + "/"; // 用户上传文件时指定的前缀。

        Map<String, String> respMap = null;
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessKeyId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));

        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return R.ok().put("data",respMap);
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}

class Codec {
    public static void main(String[] args) {

    }

    StringBuilder sb;

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        sb = new StringBuilder();
        preOrderDfs(root);
        sb.append('z');
        inOrderDfs(root);
        return sb.toString();
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        String[] orders = data.split("z");
        int len = orders[0].length();
        return buildTree(orders[0].toCharArray(), orders[1].toCharArray(), 0, len-1, 0, len-1);
    }

    public void preOrderDfs(TreeNode node) {
        if (node == null) return;
        char c = (char) ((char) node.val + 'a');
        sb.append(c);
        preOrderDfs(node.left);
        preOrderDfs(node.right);
    }

    public void inOrderDfs(TreeNode node) {
        if (node == null) return;
        inOrderDfs(node.left);
        char c = (char) ((char) node.val + 'a');
        sb.append(c);
        inOrderDfs(node.right);
    }

    public TreeNode buildTree(char[] preOrder, char[] inOrder, int preI, int preJ, int inI, int inJ) {
        if (preI > preJ) return null;
        int rootVal = preOrder[preI] - 'a';
        TreeNode root = new TreeNode(rootVal);
        int leftNum = 0;
        int i = 0;
        for (i = inI; i < inJ; i++) {
            if (inOrder[i] - 'a' == rootVal) {
                leftNum = i - inI;
                break;
            }
        }
        root.left = buildTree(preOrder, inOrder, preI + 1, preJ + leftNum, inI, i - 1);
        root.right = buildTree(preOrder, inOrder, preI + leftNum + 1, preJ, i + 1, inJ);
        return root;
    }
}
