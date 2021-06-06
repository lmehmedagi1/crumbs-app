const jpeg = ["jpg", "jpeg", "jfif", "pjpeg", "pjp"]


export const getMimeTypes = () => {
    var mimeType = {
        "apng": "image/apng",
        "gif": "image/gif",
        "png": "image/png",
        "svg": "image/webp"
    }

    jpeg.forEach(element => {
        mimeType[element] = "image/jpeg"
    });

    return mimeType
}

