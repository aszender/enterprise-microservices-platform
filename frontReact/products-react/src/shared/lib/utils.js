// shared/lib/utils.js
// Utility for merging Tailwind classes (used by shadcn/ui components)

import { clsx } from 'clsx'
import { twMerge } from 'tailwind-merge'

export function cn(...inputs) {
  return twMerge(clsx(inputs))
}
