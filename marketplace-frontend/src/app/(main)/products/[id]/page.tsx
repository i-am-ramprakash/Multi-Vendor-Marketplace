'use client';

import { useState } from 'react';
import { Star, ShoppingCart, Heart, Truck, Shield, ArrowLeft } from 'lucide-react';
import Link from 'next/link';
import { formatCurrency } from '@/lib/utils';

export default function ProductDetailPage({ params }: { params: { id: string } }) {
  const [quantity, setQuantity] = useState(1);
  const [selectedImage, setSelectedImage] = useState(0);

  const product = {
    id: params.id,
    name: 'Wireless Bluetooth Headphones',
    price: 99.99,
    compareAtPrice: 129.99,
    rating: 4.5,
    reviews: 128,
    vendor: 'TechStore',
    vendorId: 1,
    description: 'High-quality wireless headphones with noise cancellation and 30-hour battery life.',
    stockQuantity: 15,
    images: ['/placeholder.jpg', '/placeholder.jpg', '/placeholder.jpg'],
    features: [
      'Active Noise Cancellation',
      '30-hour battery life',
      'Bluetooth 5.0',
      'Foldable design',
    ],
  };

  const handleAddToCart = () => {
    // TODO: Implement add to cart
    console.log('Adding to cart:', { productId: product.id, quantity });
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <Link href="/products" className="inline-flex items-center text-gray-600 hover:text-primary mb-6">
        <ArrowLeft className="h-4 w-4 mr-2" />
        Back to Products
      </Link>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-12">
        {/* Images */}
        <div>
          <div className="aspect-square bg-gray-100 rounded-lg mb-4">
            <div className="w-full h-full flex items-center justify-center text-gray-400">
              Product Image
            </div>
          </div>
          <div className="grid grid-cols-4 gap-4">
            {product.images.map((_, index) => (
              <button
                key={index}
                onClick={() => setSelectedImage(index)}
                className={`aspect-square bg-gray-100 rounded-lg ${
                  selectedImage === index ? 'ring-2 ring-primary' : ''
                }`}
              />
            ))}
          </div>
        </div>

        {/* Details */}
        <div>
          <h1 className="text-3xl font-bold mb-2">{product.name}</h1>
          <Link href={`/vendors/${product.vendorId}`} className="text-primary hover:underline mb-4 block">
            {product.vendor}
          </Link>

          <div className="flex items-center mb-4">
            <div className="flex items-center">
              {[...Array(5)].map((_, i) => (
                <Star
                  key={i}
                  className={`h-5 w-5 ${
                    i < Math.floor(product.rating) ? 'text-yellow-400 fill-current' : 'text-gray-300'
                  }`}
                />
              ))}
            </div>
            <span className="ml-2 text-gray-600">({product.reviews} reviews)</span>
          </div>

          <div className="mb-6">
            <span className="text-3xl font-bold text-primary">{formatCurrency(product.price)}</span>
            {product.compareAtPrice && (
              <span className="ml-3 text-xl text-gray-400 line-through">
                {formatCurrency(product.compareAtPrice)}
              </span>
            )}
          </div>

          <p className="text-gray-600 mb-6">{product.description}</p>

          <div className="mb-6">
            <h3 className="font-semibold mb-3">Features:</h3>
            <ul className="space-y-2">
              {product.features.map((feature, index) => (
                <li key={index} className="flex items-center text-gray-600">
                  <span className="w-1.5 h-1.5 bg-primary rounded-full mr-2" />
                  {feature}
                </li>
              ))}
            </ul>
          </div>

          <div className="mb-6">
            <label className="block text-sm font-medium text-gray-700 mb-2">Quantity</label>
            <div className="flex items-center space-x-3">
              <button
                onClick={() => setQuantity(Math.max(1, quantity - 1))}
                className="w-10 h-10 border rounded-lg flex items-center justify-center hover:bg-gray-100"
              >
                -
              </button>
              <span className="w-12 text-center">{quantity}</span>
              <button
                onClick={() => setQuantity(Math.min(product.stockQuantity, quantity + 1))}
                className="w-10 h-10 border rounded-lg flex items-center justify-center hover:bg-gray-100"
              >
                +
              </button>
            </div>
            <p className="text-sm text-gray-500 mt-2">{product.stockQuantity} items in stock</p>
          </div>

          <div className="flex space-x-4">
            <button
              onClick={handleAddToCart}
              className="flex-1 bg-primary text-white py-3 px-6 rounded-lg font-semibold hover:bg-primary/90 flex items-center justify-center"
            >
              <ShoppingCart className="h-5 w-5 mr-2" />
              Add to Cart
            </button>
            <button className="p-3 border rounded-lg hover:bg-gray-100">
              <Heart className="h-6 w-6" />
            </button>
          </div>

          <div className="mt-8 grid grid-cols-2 gap-4">
            <div className="flex items-center text-gray-600">
              <Truck className="h-5 w-5 mr-2" />
              <span className="text-sm">Free shipping over $50</span>
            </div>
            <div className="flex items-center text-gray-600">
              <Shield className="h-5 w-5 mr-2" />
              <span className="text-sm">30-day return policy</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
