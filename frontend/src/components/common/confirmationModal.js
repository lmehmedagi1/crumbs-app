import { Button, Modal } from 'react-bootstrap'

function ConfirmationModal(props) {
    return (
        <Modal
            show={props.show}
            onHide={props.onHide}
            backdrop="static"
            keyboard={false}
        >
            <Modal.Header closeButton>
                <Modal.Title>{props.title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {props.message}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={props.onHide}>  Close </Button>
                <Button variant="primary" onClick={props.onConfirm}>{props.confirmMessage}</Button>
            </Modal.Footer>
        </Modal>
    )
}

export default ConfirmationModal;
