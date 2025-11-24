package com.zzz.familykitchen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzz.familykitchen.common.Result;
import com.zzz.familykitchen.common.ResultCode;
import com.zzz.familykitchen.pojo.dto.RecipeQueryDTO;
import com.zzz.familykitchen.pojo.entity.Recipe;
import com.zzz.familykitchen.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/recipe")
public class RecipeController {


    @Autowired
    private RecipeService recipeService;

    // 读取配置文件中的上传路径和URL前缀
    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.url-prefix}")
    private String urlPrefix;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<Page<Recipe>> page(RecipeQueryDTO queryDTO) {
        log.info("菜品分页查询: {}", queryDTO);
        Page<Recipe> pageResult = recipeService.getRecipePage(queryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增菜品
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody Recipe recipe) {
        log.info("新增菜品: {}", recipe);
        boolean success = recipeService.addRecipe(recipe);
        return success ? Result.success("新增成功") : Result.error("新增失败");
    }

    /**
     * 修改菜品
     */
    @PostMapping("/update")
    public Result<String> update(@RequestBody Recipe recipe) {
        log.info("修改菜品: {}", recipe);
        // updateById 会自动根据 ID 更新非空字段
        boolean success = recipeService.updateById(recipe);
        return success ? Result.success("修改成功") : Result.error("修改失败");
    }

    /**
     * 删除菜品 (逻辑删除)
     */
    @PostMapping("/delete")
    public Result<String> delete(@RequestParam Long id) {
        log.info("删除菜品 ID: {}", id);
        // removeById 配合 @TableLogic 注解会自动执行 update set deleted=1
        boolean success = recipeService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * ✅ 新增：菜品图片上传接口
     * 请求路径: POST /api/recipe/upload
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("image") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error(ResultCode.PARAM_ERROR, "文件不能为空");
            }

            // 1. 生成唯一文件名 (防止文件名冲突)
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String filename = UUID.randomUUID().toString() + extension;

            // 2. 确定存储目录 (存到 recipes 子目录下)
            Path dir = Paths.get(uploadPath, "recipes");
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            // 3. 保存文件到本地
            Path filePath = dir.resolve(filename);
            file.transferTo(filePath.toFile());

            // 4. 拼接访问URL (对应 WebConfig 中的资源映射)
            // 假设 urlPrefix 是 http://localhost:8080
            // 最终 URL: http://localhost:8080/images/recipes/uuid.jpg
            String fileUrl = urlPrefix + "/images/recipes/" + filename;

            log.info("菜品图片上传成功: {}", fileUrl);
            return Result.success(fileUrl);

        } catch (IOException e) {
            log.error("图片上传失败", e);
            return Result.error("上传失败: " + e.getMessage());
        }
    }
}
