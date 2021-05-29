import { Dropbox } from 'dropbox';
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


