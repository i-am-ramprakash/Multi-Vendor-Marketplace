'use client';

import { Plus, Edit, Trash2 } from 'lucide-react';

export default function AdminCommissionsPage() {
  const rules = [
    { id: 1, name: 'Default Commission', type: 'PERCENTAGE', value: 10, status: 'ACTIVE' },
    { id: 2, name: 'Electronics Commission', type: 'PERCENTAGE', value: 8, status: 'ACTIVE' },
    { id: 3, name: 'Minimum Order Fee', type: 'FIXED', value: 2.50, status: 'ACTIVE' },
  ];

  return (
    <div>
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold">Commission Rules</h1>
        <button className="bg-primary text-white px-4 py-2 rounded-lg font-semibold hover:bg-primary/90 flex items-center">
          <Plus className="h-5 w-5 mr-2" />
          Add Rule
        </button>
      </div>

      <div className="bg-white rounded-lg shadow-sm">
        <div className="p-6 border-b">
          <div className="grid grid-cols-5 gap-4 text-sm font-medium text-gray-600">
            <div>Rule Name</div>
            <div>Type</div>
            <div>Value</div>
            <div>Status</div>
            <div>Actions</div>
          </div>
        </div>
        {rules.map((rule) => (
          <div key={rule.id} className="p-6 border-b last:border-b-0">
            <div className="grid grid-cols-5 gap-4 items-center">
              <div className="font-medium">{rule.name}</div>
              <div className="text-gray-600">{rule.type}</div>
              <div className="font-semibold">
                {rule.type === 'PERCENTAGE' ? `${rule.value}%` : `$${rule.value}`}
              </div>
              <div>
                <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                  rule.status === 'ACTIVE' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                }`}>
                  {rule.status}
                </span>
              </div>
              <div className="flex space-x-2">
                <button className="p-2 hover:bg-gray-100 rounded">
                  <Edit className="h-4 w-4" />
                </button>
                <button className="p-2 hover:bg-red-100 rounded text-red-500">
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
