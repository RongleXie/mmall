<html>
<body>
<h2>Hello World!</h2>
<form name="formUpload" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="normal upload file"/>
</form>

<form name="formRichtextUpload" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="richtext upload file"/>
</form>
</body>
</html>
