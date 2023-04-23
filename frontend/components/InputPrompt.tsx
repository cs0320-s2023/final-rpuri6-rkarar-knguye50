import { annotationInfo } from "./MapBox";
import React from "react";

/**
 * Important interface that controls the input prompt where a user
 * may enter annotation text and the annotation history, that displays
 * annotations of the current session on-screen.
 */
export interface InputPromptProps {
  setShowInputPrompt: (bool: boolean) => void;
  setContents: (contents: string) => void;
  contents: string;
  setAnnotationHistory: (items: annotationInfo[]) => void;
  annotationHistory: annotationInfo[];
  clickData: {
    [key: string]: number;
  };
}

/**
 * Function that handles when the user submits an annotation for a given location
 * on the redlining overlay data. Creates a new pop-up icon on the map
 * corresponding to the location clicked.
 */
export function InputPrompt(props: InputPromptProps) {
  // handle submit function takes note of message and location
  // and adds it to become a pop-up in the annotation history.
  function handleSubmit() {
    props.setContents("");
    let annotation = props.contents.replace("\n", "");
    let popUp: annotationInfo = {
      latitude: props.clickData.latitude,
      longitude: props.clickData.longitude,
      annotation: annotation,
    };
    let newHistory = props.annotationHistory;
    newHistory.push(popUp); // add to pop-ups on screen.
    props.setAnnotationHistory(newHistory);
    props.setShowInputPrompt(false);
  }
  return (
    <div className="layer">
      <div className="container">
        <div className="inputPrompt">
          <button
            type="button"
            className="promptClose"
            onClick={() => props.setShowInputPrompt(false)}
          >
            Cancel
          </button>
          <div className="promptWrapper">
            <textarea
              autoFocus
              placeholder="ENTER ANNOTATION HERE"
              onChange={(e) => props.setContents(e.target.value)}
              value={props.contents} // clear textbox
              onKeyUp={(e) => {
                // user may click enter instead of submit button.
                if (e.key === "Enter") {
                  handleSubmit();
                }
              }}
            />
            <button
              type="submit"
              className="submitButton"
              onClick={() => handleSubmit()}
            >
              SUBMIT
            </button>
          </div>
        </div>
      </div>

      <div className="modalBg"></div>
    </div>
  );
}
