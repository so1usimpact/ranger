import React, { Component } from 'react';

import Neuron from './Neuron';

import SVG from 'components/meta/SVG';
import Coordinates from 'utils/Coordinates';
import { bRange, enumerate } from 'utils/Numbers';

export default class RangerLayer extends Component {

  constructor(props) {
    super(props);
    this.coords = new RangerLayerCoords(this.props.layerSize);
  }

  componentDidupdate(oldProps) {
    this.forceUpdate();
  }

  setCoords = () => {
    this.coords = new RangerLayerCoords(this.props.layerSize);
  }

  renderNeurons = () => {
    return enumerate(Object.entries(this.props.neurons)).map(([i, [uuid,neuron]]) => {
      return (
        <Neuron 
            key={Math.random()} 
            coords={this.coords.getNeuronCoords(i++)} 
            type={neuron.type} 
            uuid={neuron.uuid}
            s={neuron.s}
            displayNeuronInfo={() => this.props.displayNeuronInfo(this.props.l + neuron.uuid)}
        />
      )
    })
  }

  render() {
    return (
      <SVG parentCoords={this.props.coords} coords={this.coords}>
        {this.renderNeurons()}
      </SVG>
    )
  }
}

export class RangerLayerCoords extends Coordinates {

  constructor(layerSize) {
    super();
    this.x = 0;
    this.y = 0;
    this.w = 1;
    this.h = layerSize;
  }

  getNeuronCenter = (i) => {
    return [0.5, 0.5 + i];
  }

  getNeuronCoords = (i) => {
    return {
      centerX: 0.5,
      centerY: 0.5 + i,
      radius: 0.4
    }
  }
}