package stay.with.me.common.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stay.with.me.api.model.dto.BoardFileDto;
import stay.with.me.api.model.dto.FileDto;
import stay.with.me.api.model.dto.HouseFileDto;
import stay.with.me.api.model.dto.ResponseDto;
import stay.with.me.api.service.BoardService;
import stay.with.me.api.service.HouseService;
import stay.with.me.common.ResponseStatus;
import stay.with.me.common.util.ResponseUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {


    private enum IMGEXT {
        jpg, jpeg, png, webp;

        public static boolean isValidExtension(String ext) {
            for (IMGEXT validExt : values()) {
                if (validExt.name().equalsIgnoreCase(ext)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final AmazonS3Client amazonS3Client;
    private final HouseService houseService;
    private final BoardService boardService;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostMapping( "/upload")
    public ResponseEntity<ResponseDto> uploadImage(@RequestParam("file") List<MultipartFile> files,
                                                   @ModelAttribute FileDto param) {



        try {
            String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.debug("▶▶ Using bucket name = [{}]");
//            String imageBucket = System.getenv("S3_IMAGE_BUCKET");
            String imageBucket = "stay-with-me-dangsan-image-bucket";
            log.debug("▶▶ Using bucket name = [{}]", imageBucket);
//            String fileBucket = System.getenv("S3_FILE_BUCKET");
            String fileBucket = "stay-with-me-dangsan-files-bucket";
            String bucket = "",width = "",  height = "", saveFileName = "";

            // S3 저장 후 결과 URL을 모아둘 Map
            Map<String, Object> resultMap = new HashMap<>();

            for (MultipartFile file : files) {


                String fileName = file.getOriginalFilename();
                String[] fileNames = fileName.split("\\.");

                BufferedImage image = ImageIO.read(file.getInputStream());

                if(image == null) return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), "이미지가 존재하지 않습니다.", null, HttpStatus.INTERNAL_SERVER_ERROR);

                if("image".equals(param.getType())) {
                    if(!IMGEXT.isValidExtension(fileNames[1])) {
                        return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(),"이미지 파일 확장자를 확인해 주세요.", null, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    bucket = imageBucket;
                } else if("file".equals(param.getType())) {
                    bucket = fileBucket;
                }

                if(image.getWidth() < 1024) width = String.valueOf(image.getWidth());
                else width = "1024";
                if(image.getHeight() < 1024) height = String.valueOf(image.getHeight());
                else height = "1024";



                saveFileName = fileNames[0] + "_" + width + "_" + height + "." + fileNames[1];
                String fileUrl = "https://" + bucket + ".s3."+region+".amazonaws.com/" + saveFileName;
                log.debug("image width: " + width + " height: " + height);
                log.debug("file name: " + fileName);


                // S3 업로드
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());

                // DB 저장
                if ("house".equals(param.getMenu())) {

                    HouseFileDto fileDto = new HouseFileDto();
                    fileDto.setHouseFileName(fileNames[0]);
                    fileDto.setHouseFileExtension(fileNames[1]);
                    fileDto.setHouseFileWidth(Integer.parseInt(width));
                    fileDto.setHouseFileHeight(Integer.parseInt(height));
                    fileDto.setHouseDetailId(param.getId());
                    fileDto.setHouseFilePath(fileUrl);
                    fileDto.setCreatedBy(userId);
                    houseService.createFile(fileDto);


                } else if ("board".equals(param.getMenu())) {

                    if ("thumbnail".equals(param.getType())) {
                        // 썸네일 전용 DTO, 테이블에 INSERT
                        BoardFileDto fileDto = new BoardFileDto();
                        fileDto.setBoardPostId(param.getId());
                        fileDto.setFileType("thumbnail");      // DB 에도 file_type 칼럼에 저장
                        fileDto.setBoardFileName(fileNames[0]);
                        fileDto.setBoardFileExtension(fileNames[1]);
                        fileDto.setBoardFileWidth(Integer.parseInt(width));
                        fileDto.setBoardFileHeight(Integer.parseInt(height));
                        fileDto.setBoardPostId(param.getId());
                        fileDto.setBoardFilePath(fileUrl);
                        fileDto.setCreatedBy(userId);
                        boardService.createFile(fileDto);

                    } else if ("image".equals(param.getType())) {
                        // 일반 이미지 전용 처리
                        BoardFileDto fileDto = new BoardFileDto();
                        fileDto.setBoardPostId(param.getId());
                        fileDto.setFileType("IMAGE");
                        fileDto.setBoardFileName(fileNames[0]);
                        fileDto.setBoardFileExtension(fileNames[1]);
                        fileDto.setBoardFileWidth(Integer.parseInt(width));
                        fileDto.setBoardFileHeight(Integer.parseInt(height));
                        fileDto.setBoardPostId(param.getId());
                        fileDto.setBoardFilePath(fileUrl);
                        fileDto.setCreatedBy(userId);
                        boardService.createFile(fileDto);
                    }


                }

                amazonS3Client.putObject(bucket, saveFileName, file.getInputStream(), metadata);
                // 결과에 담기 (키는 원본 파일명 or 인덱스 등)
                resultMap.put(fileName, fileUrl);
            }

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), resultMap, HttpStatus.OK );
        } catch (Exception e) {
            log.error("파일 업로드 오류", e);
            return ResponseUtil.buildResponse(
                    ResponseStatus.INTERNAL_ERROR.getCode(),
                    ResponseStatus.INTERNAL_ERROR.getMessage(),
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}


