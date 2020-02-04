import React, { Component } from 'react';

export default class NeuronConnection extends Component {
  render() {
    return (
      <line
          x1={this.props.coords.x1}
          y1={this.props.coords.y1}
          x2={this.props.coords.x2}
          y2={this.props.coords.y2}
          stroke="black"
          strokeWidth="0.1"
      />
    )
  }
}
