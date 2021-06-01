import React, { useState, useEffect } from 'react'
import { listFiles, getImage } from 'components/common/dropbox'

export function CustomImage(props) {

    const imagePlaceholder = "https://www.firstfishonline.com/wp-content/uploads/2017/07/default-placeholder-700x700.png";

    const [image, setImage] = useState(imagePlaceholder);


    useEffect(() => {
        if (props.imageId)
            getImage(props.imageId, setImage);
    }, [props.imageId]);

    return (
        <div className={props.className} onClick={props.onClick ? props.onClick : null}>
            <img
                style={props.style}
                src={image}
                alt={props.alt} />
        </div>)
}

