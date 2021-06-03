import { Dropbox } from 'dropbox'
import { dropbox } from 'configs/env'

const dbx = new Dropbox({
    accessToken: dropbox.token
})

export function listFiles() {
    dbx.filesListFolder({
        path: '',
    }).then(res => console.log("Direktorij:", res))
}


export function getImage(id, f) {
    dbx.filesDownload({
        path: id
    }).then(x => {
        var reader = new FileReader();
        reader.readAsDataURL(x.result.fileBlob);
        reader.onloadend = () => f(reader.result);
    })
}

async function asyncForEach(array, callback) {
    for (let index = 0; index < array.length; index++) {
        await callback(array[index], index, array);
    }
}

export async function uploadFiles(files, path) {

    let fileIds = []
    await asyncForEach(files, async (element) => {

        let arrayBuffer = await element.arrayBuffer();
        let blob = new Blob([new Uint8Array(arrayBuffer)], { type: element.type });

        let upload = await dbx.filesUpload({ path: '/' + path + '/' + element.name, contents: blob })

        if (upload.status === 200)
            fileIds.push(upload.result.id)

        console.log("After upload", upload)
    });

    return fileIds;

}

export async function uploadFile(file, path) {
    let upload = await dbx.filesUpload({ path: '/' + path + '/' + file.name, contents: file })
    return upload.status === 200 ? upload.result.id : null
}
