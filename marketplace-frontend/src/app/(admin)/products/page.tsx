'use client';

import { CheckCircle, XCircle, Eye, Clock } from 'lucide-react';
import { formatCurrency, getStatusColor } from '@/lib/utils';

export default function AdminProductsPage() {
  const products = [
    { id: 1, name: 'Wireless Headphones', vendor: 'TechStore', price: 99.99, status: 'PENDING', date: '2024-03-15' },
    { id: 2, name: 'Running Shoes', vendor: 'SportsWorld', price: 79.99, status: 'APPROVED', date: '2024-03-10' },
    { id: 3, name: 'Smart Watch', vendor: 'GadgetZone', price: 199.99, status: 'REJECTED', date: '2024-03-08' },
  ];

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Product Moderation</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="bg-white rounded-lg shadow-sm p-6">
          <div className="flex items-center">
            <Clock className="h-8 w-8 text-yellow-500" />
            <div className="ml-4">
              <p className="text-sm text-gray-600">Pending Review</p>
              <p className="text-2xl font-bold">23</p>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-lg shadow-sm p-6">
          <div className="flex items-center">
            <CheckCircle className="h-8 w-8 text-green-500" />
            <div className="ml-4">
              <p className="text-sm text-gray-600">Approved Today</p>
              <p className="text-2xl font-bold">12</p>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-lg shadow-sm p-6">
          <div className="flex items-center">
            <XCircle className="h-8 w-8 text-red-500" />
            <div className="ml-4">
              <p className="text-sm text-gray-600">Rejected Today</p>
              <p className="text-2xl font-bold">3</p>
            </div>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-sm">
        <div className="p-6 border-b">
          <div className="grid grid-cols-6 gap-4 text-sm font-medium text-gray-600">
            <div>Product</div>
            <div>Vendor</div>
            <div>Price</div>
            <div>Status</div>
            <div>Submitted</div>
            <div>Actions</div>
          </div>
        </div>
        {products.map((product) => (
          <div key={product.id} className="p-6 border-b last:border-b-0">
            <div className="grid grid-cols-6 gap-4 items-center">
              <div className="font-medium">{product.name}</div>
              <div className="text-gray-600">{product.vendor}</div>
              <div>{formatCurrency(product.price)}</div>
              <div>
                <span className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(product.status)}`}>
                  {product.status}
                </span>
              </div>
              <div className="text-gray-600">{product.date}</div>
              <div className="flex space-x-2">
                <button className="p-2 hover:bg-gray-100 rounded" title="View">
                  <Eye className="h-4 w-4" />
                </button>
                {product.status === 'PENDING' && (
                  <>
                    <button className="p-2 hover:bg-green-100 rounded text-green-600" title="Approve">
                      <CheckCircle className="h-4 w-4" />
                    </button>
                    <button className="p-2 hover:bg-red-100 rounded text-red-600" title="Reject">
                      <XCircle className="h-4 w-4" />
                    </button>
                  </>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
