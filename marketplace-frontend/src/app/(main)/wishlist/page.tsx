'use client';

import { Heart, ShoppingCart, Trash2 } from 'lucide-react';
import Link from 'next/link';
import { formatCurrency } from '@/lib/utils';

export default function WishlistPage() {
  const wishlistItems = [
    { id: 1, name: 'Smart Watch', price: 199.99, vendor: 'GadgetHub', image: '/placeholder.jpg' },
    { id: 2, name: 'Bluetooth Speaker', price: 49.99, vendor: 'AudioPlus', image: '/placeholder.jpg' },
  ];

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h1 className="text-3xl font-bold mb-8">My Wishlist</h1>

      {wishlistItems.length === 0 ? (
        <div className="text-center py-16">
          <Heart className="h-16 w-16 text-gray-300 mx-auto mb-4" />
          <h2 className="text-xl font-semibold mb-2">Your wishlist is empty</h2>
          <p className="text-gray-600 mb-6">Save items you love for later</p>
          <Link
            href="/products"
            className="bg-primary text-white px-6 py-3 rounded-lg font-semibold hover:bg-primary/90 inline-block"
          >
            Browse Products
          </Link>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {wishlistItems.map((item) => (
            <div key={item.id} className="bg-white rounded-lg shadow-sm overflow-hidden">
              <div className="aspect-square bg-gray-100" />
              <div className="p-4">
                <h3 className="font-semibold mb-1">{item.name}</h3>
                <p className="text-sm text-gray-600 mb-2">{item.vendor}</p>
                <p className="text-lg font-bold mb-4">{formatCurrency(item.price)}</p>
                <div className="flex space-x-2">
                  <button className="flex-1 bg-primary text-white py-2 rounded-lg text-sm hover:bg-primary/90 flex items-center justify-center">
                    <ShoppingCart className="h-4 w-4 mr-1" />
                    Add to Cart
                  </button>
                  <button className="p-2 border rounded-lg hover:bg-gray-100 text-red-500">
                    <Trash2 className="h-4 w-4" />
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
