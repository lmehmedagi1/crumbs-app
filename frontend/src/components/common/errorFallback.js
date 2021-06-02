import React from "react"
import { Container } from "react-bootstrap"
import { MdErrorOutline } from "react-icons/md"

const ErrorFallback = () => (
    <Container className="text-secondary text-center text-muted">
        <h2>ERROR</h2>
        <MdErrorOutline style={{ fontSize: "20rem" }} />
        <h4>Please contact the administrator!</h4>
    </Container>
);

export default ErrorFallback;
