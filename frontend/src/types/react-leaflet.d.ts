declare module 'react-leaflet' {
  import * as React from 'react';
  import { Map as LeafletMap, TileLayer as LeafletTileLayer, LatLngExpression } from 'leaflet';

  export interface MapContainerProps extends React.HTMLAttributes<HTMLDivElement> {
    center?: LatLngExpression;
    zoom?: number;
    style?: React.CSSProperties;
    children?: React.ReactNode;
  }
  export const MapContainer: React.ForwardRefExoticComponent<MapContainerProps & React.RefAttributes<LeafletMap>>;

  export interface TileLayerProps extends React.HTMLAttributes<HTMLDivElement> {
    attribution?: string;
    url: string;
  }
  export const TileLayer: React.ForwardRefExoticComponent<TileLayerProps & React.RefAttributes<LeafletTileLayer>>;

  export interface MarkerProps {
    position: LatLngExpression;
  }
  export const Marker: React.FC<MarkerProps>;
}
