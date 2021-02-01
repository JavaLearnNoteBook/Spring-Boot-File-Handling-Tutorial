package com.ramostear.application.controller;

import com.ramostear.application.model.FileInfo;
import com.ramostear.application.util.FileUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author : ramostear
 * @date : 2019/3/8 0008-15:35
 */
@Controller
public class FileController {

    private static String fileUploadRootDir = null;

    @Value ( "${file.upload.root.dir.windows}" )
    String fileUploadRootDirWindows;

    @Value ( "${file.upload.root.dir.mac}" )
    String fileUploadRootDirMac;

    @Value ( "${file.upload.root.dir.linux}" )
    String fileUploadRootDirLinux;

    private static Map<String,FileInfo> fileRepository = new HashMap<>();

//    @Resource
    private ResourceLoader resourceLoader;

    @PostConstruct
    public void initFileRepository(){
        FileInfo file1 = new FileInfo ().setFileName ( "bg1.jpg" );
        FileInfo file2 = new FileInfo ().setFileName ( "bg2.jpg" );
        FileInfo file3 = new FileInfo ().setFileName ( "bg3.jpg" );
        fileRepository.put ( file1.getName (),file1 );
        fileRepository.put ( file2.getName (),file2 );
        fileRepository.put ( file3.getName (),file3 );

        // 判断文件夹是否存在，不存在就创建
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac OS")) {
            // 苹果
            fileUploadRootDir = fileUploadRootDirMac;
        } else if (osName.startsWith("Windows")) {
            // windows
            fileUploadRootDir = fileUploadRootDirWindows;
        } else {
            // unix or linux
            fileUploadRootDir = fileUploadRootDirLinux;
        }
        FileUtil.createDirectories(fileUploadRootDir);
    }

    @GetMapping("/files")
    public String files(Model model){
        Collection<FileInfo> files = fileRepository.values ();
        model.addAttribute ( "data",files );
        return "files";
    }


    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException {

        File convertFile = new File ( fileUploadRootDir+file.getOriginalFilename ());
        FileOutputStream fileOutputStream = new FileOutputStream ( convertFile );
        fileOutputStream.write ( file.getBytes () );
        fileOutputStream.close ();

        FileInfo fileInfo = new FileInfo()
                .setFileName ( file.getOriginalFilename());

        fileRepository.put ( fileInfo.getName (),fileInfo);

        return "File is upload successfully";
    }

    @GetMapping("/download/{fileName}")
    @ResponseBody
    public void downloadFile(@PathVariable(name = "fileName") String fileName,
                             HttpServletResponse response){
        try {
            //获取要下载的模板名称
            //设置要下载的文件的名称
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            //通知客服文件的MIME类型
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //获取文件的路径
//            String filePath = getClass().getResource("/"+fileName).getPath();
            InputStream input = getClass().getClassLoader().getResourceAsStream(fileName);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            System.out.println(filePath);
//            FileInputStream input = new FileInputStream(filePath);
            OutputStream out = response.getOutputStream();

            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.flush();
            out.close();
            input.close();
        } catch (Exception ex) {
//            log.error("getApplicationTemplate :", ex);
            System.out.println(ex);
            //return Response.ok("应用导入模板下载失败！");
        }
    }



}
