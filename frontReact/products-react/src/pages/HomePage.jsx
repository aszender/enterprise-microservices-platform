// pages/HomePage.jsx
// Simple landing page with navigation to Products

import { Link } from 'react-router-dom'
import { Button, Card, CardHeader, CardTitle, CardDescription, CardContent } from '../shared/ui'
import { Package, ArrowRight, Zap, Database, Layers } from 'lucide-react'

const features = [
  { icon: Zap, label: 'Redux Toolkit', desc: 'State management' },
  { icon: Layers, label: 'React Router', desc: 'Client-side routing' },
  { icon: Database, label: 'Spring Boot API', desc: 'RESTful backend' },
]

export default function HomePage() {
  return (
    <main className="mx-auto flex min-h-[calc(100vh-73px)] max-w-4xl flex-col items-center justify-center px-6 py-16">
      {/* Hero */}
      <div className="text-center">
        <div className="mb-4 inline-flex items-center gap-2 rounded-full border border-border/50 bg-secondary/50 px-4 py-1.5 text-sm text-muted-foreground">
          <Package className="h-4 w-4" />
          Full-Stack CRUD Demo
        </div>
        <h1 className="text-4xl font-bold tracking-tight sm:text-5xl">
          Products{' '}
          <span className="bg-gradient-to-r from-violet-500 to-purple-600 bg-clip-text text-transparent">
            React
          </span>
        </h1>
        <p className="mx-auto mt-4 max-w-md text-lg text-muted-foreground">
          A modern React app with shadcn/ui components, connected to Spring Boot REST API
        </p>
      </div>

      {/* CTA */}
      <div className="mt-8">
        <Button asChild size="lg" className="group">
          <Link to="/products">
            Manage Products
            <ArrowRight className="ml-2 h-4 w-4 transition-transform group-hover:translate-x-1" />
          </Link>
        </Button>
      </div>

      {/* Features */}
      <div className="mt-16 grid w-full gap-4 sm:grid-cols-3">
        {features.map(({ icon: Icon, label, desc }) => (
          <Card key={label} className="border-border/50 bg-card/50 backdrop-blur-sm">
            <CardContent className="flex flex-col items-center p-6 text-center">
              <div className="mb-3 flex h-10 w-10 items-center justify-center rounded-lg bg-secondary">
                <Icon className="h-5 w-5 text-muted-foreground" />
              </div>
              <p className="font-medium">{label}</p>
              <p className="mt-1 text-sm text-muted-foreground">{desc}</p>
            </CardContent>
          </Card>
        ))}
      </div>
    </main>
  )
}
