/*
 * Solo - A small and beautiful blogging system written in Java.
 * Copyright (c) 2010-present, b3log.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.b3log.solo.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.RandomStringUtils;
import org.b3log.latke.Latkes;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.json.JSONObject;

/**
 * Files utilities.
 * @author zhuangyilian
 */
public final class Files {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(Files.class);

    /**
     * Upload File processing.
     * 
     * @author zhuangyilian
     * @param request the specified request
     * @param filePath file storage path
     * @return return result data
     * @throws FileUploadException 
     */
    public static JSONObject upload(final HttpServletRequest request, final String filePath) throws FileUploadException {
        List<String> errFiles = new ArrayList<String>();
        JSONObject succMap = new JSONObject();
        
        String realPath = request.getServletContext().getRealPath(filePath);
        File realDir = new File (realPath);
        if (!realDir.exists()) {
            realDir.mkdirs(); 
        }
        String datePath = createDateDir(realPath);
        String fileName = null;
        String localFileName = null;
        
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        servletFileUpload.setHeaderEncoding("UTF-8");
        List<FileItem> fileItems = servletFileUpload.parseRequest(request);
        for(FileItem fileItem : fileItems){
            if(fileItem.isFormField()){
                continue;
            }

            try {
            	fileName = fileItem.getName();
            	localFileName = localFileName(fileName);
                String absFilePath = realPath + datePath + File.separator + localFileName;
                fileItem.write(new File(absFilePath));
                
                String fileUrl = Latkes.getServePath() + filePath + datePath.replace(File.separator, "/") + "/" + localFileName;
                succMap.put(fileName, fileUrl);
            } catch (Exception e) {
                LOGGER.log(Level.ERROR, "Upload file failed", e);

                errFiles.add(fileName);
            }
        }

        JSONObject resultData = new JSONObject();
        resultData.put("errFiles", errFiles.toArray());
        resultData.put("succMap", succMap);
        return resultData;
    }
    
    /**
     * Create date directory.
     * 
     * @author zhuangyilian
     * @param realPath
     * @return return date directory path
     */
    public static String createDateDir(String realPath) {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	String dateStr = simpleDateFormat.format(new Date());
    	String[] dateField = dateStr.split("-");
    	String datePath = "";
    	for (String field : dateField) {
    		datePath = datePath + File.separator + field;
		}
    	
    	File dateDir = new File(realPath, datePath);
    	if (!dateDir.exists()) {
    		dateDir.mkdirs();
    	}
    	
    	return datePath;
    }
    
    /**
     * Build local file name.
     * 
     * @author zhuangyilian
     * @param fileName
     * @return return local file name
     */
    public static String localFileName(String fileName) {
    	String[] fileNames = fileName.split("\\.");
    	String preName = fileNames[0] + "-" + RandomStringUtils.randomNumeric(8);
    	String localFileName = fileName.replace(fileNames[0], preName);
    	
    	return localFileName;
    }

    /**
     * Private constructor.
     */
    private Files() {
    }
}
