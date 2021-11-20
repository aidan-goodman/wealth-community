package top.aidan.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import top.aidan.community.annotation.LoginRequired;
import top.aidan.community.entity.User;
import top.aidan.community.service.UserService;
import top.aidan.community.util.CommunityUtil;
import top.aidan.community.util.HostHolder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Aidan
 * @createTime 2021/11/18 9:52
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String DOT = ".";

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${community.path.domain}")
    private String domain;

    /**
     * 通过配置文件读取存储路径
     */
    @Value("${community.path.uploadPath}")
    private String uploadPath;

    private final UserService userService;

    private final HostHolder hostHolder;

    public UserController(UserService userService, HostHolder hostHolder) {
        this.userService = userService;
        this.hostHolder = hostHolder;
    }

    /**
     * 返回个人设置页面
     *
     * @return setting Page
     */
    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    /**
     * 直接在控制层进行处理，低耦合
     *
     * @param headerImage 用户上传的图片
     * @param model       存储错误信息
     * @return 根据逻辑返回响应页面
     */
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "上传失败，您还没有选择图片");
            return "/site/setting";
        }

        // 处理图片文件
        String filename = headerImage.getOriginalFilename();
        String suffix = null;
        if (filename != null && filename.contains(DOT)) {
            suffix = Objects.requireNonNull(filename).substring(filename.lastIndexOf("."));
        }
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "图片格式不正确");
            return "/site/setting";
        }

        // 防止用户名冲突，使用 UUID 作为文件名
        filename = CommunityUtil.generateUUID() + suffix;

        File dest = new File(uploadPath + "/" + filename);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            String error = "文件上传失败";
            logger.error(error, e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常！", e);
        }

        // 封装图片为链接格式：context-Path/domain/user/header/filename
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + filename;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";

    }

    /**
     * 使用 IO 流进行文件的的读取
     *
     * @param fileName 获取路径上的文件名
     * @param response 通过 response 写回界面
     */
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {

        // 通过存储路径确定具体图片
        fileName = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        // 根据文件后缀确定前端格式
        response.setContentType("image/" + suffix);

        // 读取并写入
        try (
                FileInputStream fis = new FileInputStream(fileName)
        ) {
            ServletOutputStream os = response.getOutputStream();
            byte[] data = new byte[1024];
            int b;
            while ((b = fis.read(data)) != -1) {
                os.write(data, 0, b);
            }
        } catch (IOException e) {
            String error = "读取头像失败";
            logger.error(error, e.getMessage());
        }

    }

    /**
     * 进行密码的修改，后续封装入 Service 方法
     *
     * @param oldPassword     原密码
     * @param newPassword     新密码
     * @param confirmPassword 确认新密码
     * @param model           逻辑信息
     * @return 返回逻辑页面
     */
    @LoginRequired
    @RequestMapping(path = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(String oldPassword, String newPassword, String confirmPassword, Model model) {

        User user = hostHolder.getUser();
        if (!user.getPassword().equals(CommunityUtil.md5(oldPassword + user.getSalt()))) {
            model.addAttribute("old_error", "原密码输入错误！");
            return "/site/setting";
        }

        int pwLength = 8;
        if (StringUtils.isBlank(newPassword) || newPassword.length() < pwLength) {
            model.addAttribute("new_error", "密码长度不能小于 8！");
            return "/site/setting";
        }
        if (StringUtils.isBlank(confirmPassword) || !confirmPassword.equals(newPassword)) {
            model.addAttribute("confirm_error", "请确认密码！");
            return "/site/setting";
        }

        userService.updatePassword(user.getId(), CommunityUtil.md5(newPassword + user.getSalt()));

        return "redirect:/login";
    }

}
