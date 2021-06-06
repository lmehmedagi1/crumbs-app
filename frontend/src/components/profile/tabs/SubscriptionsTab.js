import CustomTable from 'components/profile/tabs/table';
import React from 'react';


function SubscriptionsTab(props) {

    return (
        <div className="subscriptionsTab">
            <div>
            <CustomTable tab={"subscribers"} {...props} />
            </div>
            <div>
            <CustomTable tab={"subscriptions"} {...props} />
            </div>
        </div>
    )
}

export default SubscriptionsTab;
