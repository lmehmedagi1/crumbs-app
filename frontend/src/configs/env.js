export const env = {
    BASE_PATH: !process.env.NODE_ENV || process.env.NODE_ENV === "development"
        ? "http://localhost:8090/"
        : "/crumbs/",
};


export const dropbox = {
    token: "uUFTXpAbe60AAAAAAAAAARSwea0wdebEtT7VZv_ySpT5gaOQsg4y7qmUQXZQ1VJ_"
}