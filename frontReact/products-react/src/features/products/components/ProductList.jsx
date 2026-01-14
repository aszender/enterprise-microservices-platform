// features/products/components/ProductList.jsx
// Displays product list with edit/delete actions (mirrors Vue ProductList.vue)

import { Pencil, Trash2, Package, DollarSign } from 'lucide-react'
import { Button, Card, CardHeader, CardTitle, CardContent } from '../../../shared/ui'

function formatPrice(price) {
  if (price == null) return ''
  return Number(price).toFixed(2)
}

export default function ProductList({ products, busy, onEdit, onDelete }) {
  return (
    <Card className="border-border/50 bg-card/50 backdrop-blur-sm">
      <CardHeader className="pb-4">
        <CardTitle className="flex items-center gap-2 text-lg">
          <Package className="h-5 w-5 text-muted-foreground" />
          Products
        </CardTitle>
      </CardHeader>
      <CardContent>
        {products?.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-8 text-center">
            <Package className="mb-3 h-12 w-12 text-muted-foreground/50" />
            <p className="text-sm text-muted-foreground">No products yet.</p>
            <p className="mt-1 text-xs text-muted-foreground/70">Create your first product using the form.</p>
          </div>
        ) : (
          <ul className="-mx-2 divide-y divide-border/50">
            {(products ?? []).map((p) => (
              <li key={p.id} className="flex items-start justify-between gap-4 rounded-lg px-2 py-4 transition-colors hover:bg-secondary/30">
                <div className="min-w-0 text-left">
                  <div className="flex flex-wrap items-center gap-2">
                    <span className="font-semibold">{p.name}</span>
                    <span className="rounded bg-secondary px-1.5 py-0.5 text-[10px] font-medium text-muted-foreground">#{p.id}</span>
                  </div>
                  {p.description && (
                    <p className="mt-1.5 text-sm leading-relaxed text-muted-foreground">{p.description}</p>
                  )}
                  <p className="mt-2 flex items-center gap-1 text-sm font-semibold text-green-400">
                    <DollarSign className="h-3.5 w-3.5" />
                    {formatPrice(p.price)}
                  </p>
                </div>

                <div className="flex shrink-0 gap-1.5">
                  <Button
                    variant="ghost"
                    size="icon"
                    className="h-8 w-8"
                    disabled={busy}
                    onClick={() => onEdit?.(p)}
                  >
                    <Pencil className="h-4 w-4" />
                    <span className="sr-only">Edit</span>
                  </Button>
                  <Button
                    variant="ghost"
                    size="icon"
                    className="h-8 w-8 text-destructive hover:bg-destructive/10 hover:text-destructive"
                    disabled={busy}
                    onClick={() => onDelete?.(p)}
                  >
                    <Trash2 className="h-4 w-4" />
                    <span className="sr-only">Delete</span>
                  </Button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </CardContent>
    </Card>
  )
}
