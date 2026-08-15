'use client';

import { Plus, Edit, Trash2, Eye } from 'lucide-react';
import Link from 'next/link';
import { formatCurrency, getStatusColor } from '@/lib/utils';

export default function VendorProductsPage() {
  const products = [
    { id: 1, name: 'Wireless Headphones', price: 99.99, stock: 15, status: 'APPROVED', sales: 45 },
    { id: 2, name: 'Running Shoes', price: 79.99, stock: 23, status: 'APPROVED', sales: 32 },
    { id: 3, name: 'Smart Watch', price: 199.99, stock: 8, status: 'PENDING', sales: 0 },
  ];

  return (
    <div>
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold">My Products</h1>
        <Link
          href="/products/new"
          className="bg-primary text-white px-4 py-2 rounded-lg font-semibold hover:bg-primary/90 flex items-center"
        >
          <Plus className="h-5 w-5 mr-2" />
          Add Product
        </Link>
      </div>

      <div className="bg-white rounded-lg shadow-sm">
        <div className="p-6 border-b">
          <div className="grid grid-cols-6 gap-4 text-sm font-medium text-gray-600">
            <div>Product</div>
            <div>Price</div>
            <div>Stock</div>
            <div>Sales</div>
            <div>Status</div>
            <div>Actions</div>
          </div>
        </div>
        {products.map((product) => (
          <div key={product.id} className="p-6 border-b last:border-b-0">
            <div className="grid grid-cols-6 gap-4 items-center">
              <div className="font-medium">{product.name}</div>
              <div>{formatCurrency(product.price)}</div>
              <div>{product.stock} units</div>
              <div>{product.sales} sold</div>
              <div>
                <span className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(product.status)}`}>
                  {product.status}
                </span>
              </div>
              <div className="flex space-x-2">
                <button className="p-2 hover:bg-gray-100 rounded">
                  <Eye className="h-4 w-4" />
                </button>
                <button className="p-2 hover:bg-gray-100 rounded">
                  <Edit className="h-4 w-4" />
                </button>
                <button className="p-2 hover:bg-gray-100 rounded text-red-500">
                  <Trash2 className="h-4 w-4" />
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
