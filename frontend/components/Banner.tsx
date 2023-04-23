import React from "react";

/**
 * Interface for top-banner props. Includes the viewState that
 * stores longitude, latitude, and zoom to be visible to users.
 */
export interface BannerProps {
  viewState: {
    [key: string]: number;
  };
}

/**
 * Banner function that creates and returns the top banner
 * to display the longitude, latitude, and zoom as a user naviagates the map.
 * @param props
 * @returns
 */
export function Banner(props: BannerProps) {
  return (
    <div className="banner">
      Longitude: {props.viewState.longitude.toFixed(6)} | Latitude:{" "}
      {props.viewState.latitude.toFixed(6)} | Zoom:{" "}
      {props.viewState.zoom.toFixed(1)}
    </div>
  );
}
