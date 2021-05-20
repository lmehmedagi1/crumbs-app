export const env = {
    BASE_PATH: !process.env.NODE_ENV || process.env.NODE_ENV === "development"
    ? "http://localhost:8090/"
    : "/crumbs/",
};
