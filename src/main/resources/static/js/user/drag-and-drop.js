$(function () {
    var oDiv = $("html").get(0);
    oDiv.ondragenter = function () {
        $("#dragToUploadInformation").show(1000);
        setTimeout(function () {
            $("#dragToUploadInformation").hide(1000);
        }, 2000);
    };
    oDiv.ondragover = function (e) {
        e.preventDefault();
    };
    oDiv.ondragleave = function (e) {
        e.preventDefault();
    };
    oDiv.ondrop = function (e) {
        e.preventDefault();
        var fs = e.dataTransfer.files;
        for (var i = 0; i < fs.length; i++) {
            suffixName = fs[i].name.split(".");
            suffixName = suffixName[suffixName.length - 1];
            suffixName = suffixName.toLowerCase();
            if (suffixName == "jpeg" || suffixName == "jpg" || suffixName == "png" || suffixName == "gif" || suffixName == "svg") {
                uploadToServer(fs[i]);
            } else {
                sendInnerNotify(fs[i].name + " 格式不受支持，将跳过该图片的上传。（其它图片不受影响）");
            }
        }
    }
});

function dataURLtoFile(dataURL, fileName) {
    var arr = dataURL.split(','),
        mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]),
        n = bstr.length,
        u8arr = new Uint8Array(n);
    while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
    }
    return new File([u8arr], fileName, {type: mime});
}