import React, { Component } from "react"
import { Dropdown, Pagination } from "react-bootstrap"
import "./paginator.scss"


export class Paginator extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    var size = this.props.pageSize
    var numbers = [
      ...Array(size).keys()
    ];
    var before = true;
    var after = true;
    var items = numbers.map(i => {
  if (i > this.props.page && after) {
        after = false;
        return <Pagination.Ellipsis key="after" />
      }
      else if (i + 2 < this.props.page && before) {
        before = false;
        return <Pagination.Ellipsis key="before" />
      }
    });

    return (
      <div className="paginator-wrapper">
        <Dropdown>
          <Dropdown.Toggle size="sm" variant="primary" id="dropdown-basic">
            {this.props.pageSize}
          </Dropdown.Toggle>

          <Dropdown.Menu>
            <Dropdown.Item id={10} onClick={() => this.props.onPageSizeChange(10)}>10</Dropdown.Item>
            <Dropdown.Item id={25} onClick={() => this.props.onPageSizeChange(25)}>25</Dropdown.Item>
            <Dropdown.Item id={50} onClick={() => this.props.onPageSizeChange(50)}>50</Dropdown.Item>
            <Dropdown.Item id={100} onClick={() => this.props.onPageSizeChange(100)}>100</Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown>
        <Pagination>
          <Pagination.First onClick={() => this.props.onPageChange(1)} />
          <Pagination.Prev onClick={() => { if (this.props.page !== 1) this.props.onPageChange(this.props.page - 1) }} />
          <Pagination.Next onClick={() => { if (this.props.page !== numbers[numbers.length - 1] + 1) this.props.onPageChange(this.props.page + 1) }} />
          <Pagination.Last onClick={() => this.props.onPageChange(numbers[numbers.length - 1] + 1)} />
        </Pagination>
      </div>
    );
  }
}
