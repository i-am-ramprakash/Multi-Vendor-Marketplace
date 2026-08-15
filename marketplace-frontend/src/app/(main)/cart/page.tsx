'use client';

import { Trash2, Plus, Minus, ShoppingBag } from 'lucide-react';
import Link from 'next/link';
import { formatCurrency } from '@/lib/utils';

export default function CartPage() {
  const cartItems = [
    { id: 1, name: 'Wireless Headphones', price: 99.99, quantity: 2, image: '/placeholder.jpg' },
    { id: 2, name: 'Running Shoes', price: 79.99, quantity: 1, image: '/placeholder.jpg' },
  ];

  const subtotal = cartItems.reduce((sum, item) => sum + item.price * item.quantity, 0);
  const shipping = subtotal > 50 ? 0 : 9.99;
  const total = subtotal + shipping;

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h1 className="text-3xl font-bold mb-8">Shopping Cart</h1>

      {cartItems.length === 0 ? (
        <div className="text-center py-16">
          <ShoppingBag className="h-16 w-16 text-gray-300 mx-auto mb-4" />
          <h2 className="text-xl font-semibold mb-2">Your cart is empty</h2>
          <p className="text-gray-600 mb-6">Start shopping to add items to your cart</p>
          <Link
            href="/products"
            className="bg-primary text-white px-6 py-3 rounded-lg font-semibold hover:bg-primary/90 inline-block"
          >
            Browse Products
          </Link>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <div className="lg:col-span-2">
            <div className="bg-white rounded-lg shadow-sm">
              {cartItems.map((item) => (
                <div key={item.id} className="p-6 border-b last:border-b-0">
                  <div className="flex items-center">
                    <div className="w-20 h-20 bg-gray-100 rounded-lg mr-4" />
                    <div className="flex-1">
                      <h3 className="font-semibold">{item.name}</h3>
                      <p className="text-gray-600">{formatCurrency(item.price)}</p>
                    </div>
                    <div className="flex items-center space-x-3">
                      <button className="w-8 h-8 border rounded flex items-center justify-center hover:bg-gray-100">
                        <Minus className="h-4 w-4" />
                      </button>
                      <span className="w-8 text-center">{item.quantity}</span>
                      <button className="w-8 h-8 border rounded flex items-center justify-center hover:bg-gray-100">
                        <Plus className="h-4 w-4" />
                      </button>
                    </div>
                    <div className="ml-6 text-right">
                      <p className="font-semibold">{formatCurrency(item.price * item.quantity)}</p>
                      <button className="text-red-500 hover:text-red-600 mt-2">
                        <Trash2 className="h-4 w-4" />
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div className="lg:col-span-1">
            <div className="bg-white rounded-lg shadow-sm p-6">
              <h2 className="text-xl font-semibold mb-4">Order Summary</h2>
              <div className="space-y-3 mb-6">
                <div className="flex justify-between">
                  <span className="text-gray-600">Subtotal</span>
                  <span>{formatCurrency(subtotal)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Shipping</span>
                  <span>{shipping === 0 ? 'Free' : formatCurrency(shipping)}</span>
                </div>
                <div className="border-t pt-3">
                  <div className="flex justify-between font-semibold text-lg">
                    <span>Total</span>
                    <span>{formatCurrency(total)}</span>
                  </div>
                </div>
              </div>
              <Link
                href="/checkout"
                className="w-full bg-primary text-white py-3 rounded-lg font-semibold hover:bg-primary/90 block text-center"
              >
                Proceed to Checkout
              </Link>
              <Link href="/products" className="w-full text-center block mt-4 text-primary hover:underline">
                Continue Shopping
              </Link>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
