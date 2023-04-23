import React from "react";

/**
 * Save Message prompt interface. Controls if prompt is
 * able to be seen and the confirmation message shown on saving.
 */
export interface SaveMessageProps {
  setShowConfirm: (bool: boolean) => void;
  confirmMsg: string;
}

/**
 * Function that creates and returns the message that appears after saving annotations
 * and allows users to dismiss the message via a cancel button.
 * @param props
 * @returns
 */
export function SaveMessage(props: SaveMessageProps) {
  return (
    <div className="layer">
      <div className="container">
        <div className="inputPrompt">
          <button
            type="button"
            className="promptClose"
            onClick={() => props.setShowConfirm(false)}
          >
            Cancel
          </button>
          <p>{props.confirmMsg}</p>
        </div>
      </div>

      <div className="modalBg"></div>
    </div>
  );
}
