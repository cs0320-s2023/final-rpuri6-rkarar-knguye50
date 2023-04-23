import React from "react";

/**
 * Props for the "Add Annotation" prompt. Controls whether it is on-screen.
 */
export interface AnnoProps {
  setShowInputPrompt: (bool: boolean) => void;
}

/**
 * Function that creates and returns the html buttton that controls
 * the prompt for adding annotations. The botton triggers the prompt to appear on-screen
 * for users to fill out a new annotation.
 * @param props
 * @returns
 */
export function AddAnno(props: AnnoProps) {
  return (
    <div className="AddAnno">
      <button
        type="button"
        className="AddAnnoButton"
        onClick={() => props.setShowInputPrompt(true)}
      >
        Add Annotation
      </button>
    </div>
  );
}
