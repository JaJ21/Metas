import { type InputHTMLAttributes, forwardRef } from "react";
import { clsx } from "@/lib/clsx";

type Props = InputHTMLAttributes<HTMLInputElement> & {
  label?: string;
  erro?: string;
};

export const Input = forwardRef<HTMLInputElement, Props>(function Input(
  { label, erro, id, className, ...rest },
  ref
) {
  const inputId = id ?? rest.name;
  return (
    <div className="flex flex-col gap-1">
      {label && (
        <label htmlFor={inputId} className="text-sm font-medium text-gray-700">
          {label}
        </label>
      )}
      <input
        ref={ref}
        id={inputId}
        aria-invalid={!!erro}
        aria-describedby={erro ? `${inputId}-erro` : undefined}
        className={clsx(
          "rounded-md border px-3 py-2 text-sm focus-visible:outline focus-visible:outline-2 focus-visible:outline-primary",
          erro ? "border-danger" : "border-gray-300",
          className
        )}
        {...rest}
      />
      {erro && (
        <span id={`${inputId}-erro`} role="alert" className="text-xs text-danger">
          {erro}
        </span>
      )}
    </div>
  );
});
