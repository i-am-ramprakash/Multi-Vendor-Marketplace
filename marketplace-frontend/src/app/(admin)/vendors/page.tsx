'use client';

import { CheckCircle, XCircle, Ban, RotateCcw, Eye } from 'lucide-react';
import { formatDate, getStatusColor } from '@/lib/utils';

export default function AdminVendorsPage() {
  const vendors = [
    { id: 1, name: 'TechStore', email: 'tech@store.com', status: 'APPROVED', date: '2024-01-15', sales: 4500 },
    { id: 2, name: 'FashionHub', email: 'fashion@hub.com', status: 'PENDING', date: '2024-03-10', sales: 0 },
    { id: 3, name: 'SportsWorld', email: 'sports@world.com', status: 'APPROVED', date: '2024-02-20', sales: 3200 },
    { id: 4, name: 'GadgetZone', email: 'gadget@zone.com', status: 'SUSPENDED', date: '2024-01-05', sales: 1500 },
  ];

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Vendor Management</h1>

      <div className="bg-white rounded-lg shadow-sm">
        <div className="p-6 border-b">
          <div className="grid grid-cols-6 gap-4 text-sm font-medium text-gray-600">
            <div>Vendor</div>
            <div>Email</div>
            <div>Status</div>
            <div>Joined</div>
            <div>Total Sales</div>
            <div>Actions</div>
          </div>
        </div>
        {vendors.map((vendor) => (
          <div key={vendor.id} className="p-6 border-b last:border-b-0">
            <div className="grid grid-cols-6 gap-4 items-center">
              <div className="font-medium">{vendor.name}</div>
              <div className="text-gray-600">{vendor.email}</div>
              <div>
                <span className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(vendor.status)}`}>
                  {vendor.status}
                </span>
              </div>
              <div className="text-gray-600">{formatDate(vendor.date)}</div>
              <div className="font-semibold">${vendor.sales.toLocaleString()}</div>
              <div className="flex space-x-2">
                <button className="p-2 hover:bg-gray-100 rounded" title="View">
                  <Eye className="h-4 w-4" />
                </button>
                {vendor.status === 'PENDING' && (
                  <>
                    <button className="p-2 hover:bg-green-100 rounded text-green-600" title="Approve">
                      <CheckCircle className="h-4 w-4" />
                    </button>
                    <button className="p-2 hover:bg-red-100 rounded text-red-600" title="Reject">
                      <XCircle className="h-4 w-4" />
                    </button>
                  </>
                )}
                {vendor.status === 'APPROVED' && (
                  <button className="p-2 hover:bg-orange-100 rounded text-orange-600" title="Suspend">
                    <Ban className="h-4 w-4" />
                  </button>
                )}
                {vendor.status === 'SUSPENDED' && (
                  <button className="p-2 hover:bg-green-100 rounded text-green-600" title="Reactivate">
                    <RotateCcw className="h-4 w-4" />
                  </button>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
