// features/products/components/ProductForm.jsx
// Reusable form for create/update (mirrors Vue ProductForm.vue)

import { useEffect, useState } from 'react'
import { Plus, Save, X } from 'lucide-react'
import { Button, Input, Textarea, Label, Card, CardHeader, CardTitle, CardContent, CardFooter } from '../../../shared/ui'

export default function ProductForm({ initialProduct, busy, onSave, onCancel }) {
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [price, setPrice] = useState('')

  // Sync form when editing product changes
  useEffect(() => {
    setName(initialProduct?.name ?? '')
    setDescription(initialProduct?.description ?? '')
    setPrice(initialProduct?.price != null ? String(initialProduct.price) : '')
  }, [initialProduct])

  const isEditMode = Boolean(initialProduct?.id)

  function handleSubmit(e) {
    e.preventDefault()
    const trimmedName = name.trim()
    const numericPrice = Number(price)

    if (!trimmedName) {
      alert('Name is required')
      return
    }
    if (Number.isNaN(numericPrice)) {
      alert('Price must be a number')
      return
    }

    onSave?.({
      name: trimmedName,
      description: description.trim(),
      price: numericPrice,
    })
  }

  return (
    <Card className="border-border/50 bg-card/50 backdrop-blur-sm">
      <CardHeader className="pb-4">
        <CardTitle className="flex items-center gap-2 text-lg">
          {isEditMode ? (
            <><Save className="h-5 w-5 text-muted-foreground" /> Edit product</>
          ) : (
            <><Plus className="h-5 w-5 text-muted-foreground" /> Create product</>
          )}
        </CardTitle>
      </CardHeader>
      <form onSubmit={handleSubmit}>
        <CardContent className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="name">Name</Label>
            <Input
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Product name"
              disabled={busy}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="description">Description</Label>
            <Textarea
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Optional description"
              rows={3}
              disabled={busy}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="price">Price</Label>
            <Input
              id="price"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              placeholder="e.g. 19.99"
              inputMode="decimal"
              disabled={busy}
            />
          </div>
        </CardContent>
        <CardFooter className="gap-3">
          <Button type="submit" disabled={busy}>
            {isEditMode ? 'Update' : 'Create'}
          </Button>
          {isEditMode && (
            <Button type="button" variant="outline" disabled={busy} onClick={onCancel}>
              Cancel
            </Button>
          )}
        </CardFooter>
      </form>
    </Card>
  )
}
